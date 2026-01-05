package com.sista.georeportapi.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CsvImportResult {
    int created;
    List<String> errors;
}
