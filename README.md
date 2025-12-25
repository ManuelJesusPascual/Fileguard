# FileGuard API

FileGuard API is a lightweight Java Spring Boot service for validating CSV, JSON, and XML files against user-defined schemas. It detects errors such as missing required fields, type mismatches, duplicates, and empty rows. Designed for developers and teams who need a simple, automated file validation pipeline.

## Live Demo

The API is deployed and ready to use:

Base URL: https://fileguard-production.up.railway.app

Swagger URL: https://fileguard-production.up.railway.app/swagger-ui/index.html

Validation Endpoint: https://fileguard-production.up.railway.app/api/validate

You can test the live API using curl, Postman, or any HTTP client.

## Features

- Validate CSV, JSON, and XML files
- Schema-driven: define required columns, data types, min/max values
- Detects missing values, duplicates, and format errors
- JSON response with detailed error report
- Lightweight, backend-only API (no UI required)
- Ready for deployment with Docker
- Swagger / OpenAPI documentation included

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker (optional, for deployment)

### Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/fileguard-api.git
cd fileguard-api
