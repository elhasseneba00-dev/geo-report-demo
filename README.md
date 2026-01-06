# GeoReports project

Mini-projet (4h) : API Spring Boot + PostgreSQL pour collecter des observations terrain, produire du GeoJSON et des stats.  
Inclut Docker, docker-compose et CI GitHub Actions (build + push DockerHub).

## Stack
- Spring Boot 3 / Java 17
- PostgreSQL
- Docker / docker-compose
- GitHub Actions
- Postman

## Lancer en local (Docker)
```bash
docker compose up --build
```

API: http://localhost:8085  
Health: http://localhost:8085/actuator/health

## Endpoints
### Create observation
`POST /api/observations`

Example JSON:
```json
{
  "title": "Lampadaire en panne",
  "type": "INFRA",
  "description": "Quartier Tevragh Zeina",
  "lat": 18.092,
  "lon": -15.978,
  "observedAt": "2026-01-04T10:00:00Z",
  "source": "manual"
}
```

### List observations
`GET /api/observations?type=INFRA&from=2026-01-01T00:00:00Z&to=2026-01-10T00:00:00Z`

Filter box (minLon,minLat,maxLon,maxLat):
`GET /api/observations?bbox=-16.1,18.0,-15.9,18.2`

### GeoJSON
`GET /api/observations/geojson`

### Stats by type
`GET /api/stats/observations-by-type`

## Postman
Importer la collection dans `postman/`.

## Import CSV (collecte des données)
Endpoint:
`POST /api/import/observations/csv` (multipart/form-data)

CSV header attendu:
`title,type,description,lat,lon,observedAt,source`

Exemple (curl):
```bash
curl -X POST http://localhost:8085/api/import/observations/csv \
  -F "file=@observations.csv"
```
