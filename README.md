# Hackathon - Data Migration

Used to migrate data from a given file (CSV) to a given structure.

# Building
```cmd
./mvnw package
```

# Install
```cmd
./mvnw install
```

# Build image
```cmd
./mvnw package dockerfile:build
```

# Run docker image
```cmd
docker-compose up -d
```

# Usage

## Upload a file to the server.

```http
POST http://localhost:8082/migration/upload
```

Response:

```json
{ 
  "filename": "filename.xyz"
} 
```

## Get columns.

```http
GET http://localhost:8082/migration/{filename}
```

Response:

```json
{
    "columns": [
        "sku/id",
        "Name",
        "short_desc",
        "desc",
        "price",
        "wholesale_price",
        "avalaible_qtd",
        "unit_multiplier",
        "Option Desc"
    ],
    "sampleData": [
        {
            "Option Desc": "Vanilla Scent",
            "price": "1",
            "wholesale_price": "2",
            "sku/id": "2311",
            "unit_multiplier": "10",
            "short_desc": "Fantastic Candle",
            "avalaible_qtd": "100",
            "Name": "Candle",
            "desc": "Fantastic Candle"
        },
        {
            "Option Desc": "Chocolate Scent",
            "price": "1",
            "wholesale_price": "2",
            "sku/id": "2311",
            "unit_multiplier": "10",
            "short_desc": "Fantastic Candle",
            "avalaible_qtd": "100",
            "Name": "Candle",
            "desc": "Fantastic Candle"
        }
    ]
}
```

## Parse to a specific structure:

```cmd
POST http://localhost:8082/migration/data.migration.csv
```

Body

```json
{ 
	"columns": [
		{ "csvColumn": "sku/id" , "fieldName": "id" },
		{ "csvColumn": "Name" , "fieldName": "name" }
	]
}
```

Response:

```json
{
    "columns": [
        {
            "csvColumnIndex": 0,
            "csvColumn": "sku/id",
            "fieldName": "id"
        },
        {
            "csvColumnIndex": 1,
            "csvColumn": "Name",
            "fieldName": "name"
        }
    ],
    "values": [
        {
            "name": "Candle",
            "id": "2311"
        },
        {
            "name": "Candle",
            "id": "2311"
        }
    ]
}
```

