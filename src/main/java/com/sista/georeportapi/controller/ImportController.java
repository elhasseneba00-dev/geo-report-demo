package com.sista.georeportapi.controller;

import com.sista.georeportapi.dto.CsvImportResult;
import com.sista.georeportapi.dto.ObservationRequest;
import com.sista.georeportapi.model.ObservationType;
import com.sista.georeportapi.service.ObservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final ObservationService observationService;

    @PostMapping(value = "/observations/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CsvImportResult> importCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body(CsvImportResult.builder()
                    .created(0)
                    .errors(List.of("Le fichier est vide") )
                    .build()
            );
        }

        int created = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = br.readLine();
            if (header == null) {
                return ResponseEntity.badRequest().body(CsvImportResult.builder()
                        .created(0)
                        .errors(List.of("missing header row"))
                        .build());
            }

            // expected: titre,type,description,lat,lon,observedAt,source
            int lineNo = 1;
            String line;
            while ((line = br.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) continue;

                // Simple CSV parser (assumes no commas inside fields)
                String[] cols = line.split(",", -1);
                if (cols.length < 7) {
                    errors.add("Ligne N°" + lineNo + ": doit avoir 7 colonnes, mais il y'a " + cols.length);
                    continue;
                }

                try {
                    ObservationRequest request = new ObservationRequest();
                    request.setTitre(cols[0].trim());
                    request.setType(ObservationType.valueOf(cols[1].trim()));
                    request.setDescription(StringUtils.hasText(cols[2]) ? cols[2].trim() : null);
                    request.setLat(Double.parseDouble(cols[3].trim()));
                    request.setLon(Double.parseDouble(cols[4].trim()));
                    request.setObservationAt(StringUtils.hasText(cols[5]) ? Instant.parse(cols[5].trim()) : null);
                    request.setSource(StringUtils.hasText(cols[6]) ? cols[6].trim() : "importer");

                    observationService.create(request);
                    created++;
                } catch (Exception ex) {
                    errors.add("Ligne N°" + lineNo + ": " + ex.getMessage());
                }
            }

            return ResponseEntity.ok(CsvImportResult.builder()
                    .created(created)
                    .errors(errors)
                    .build());

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(CsvImportResult.builder()
                    .created(created)
                    .errors(List.of("Erreur de lecture de fichier: " + ex.getMessage()))
                    .build());
        }
    }
}
