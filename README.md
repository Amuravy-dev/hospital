# Sistema de Gestión de Citas Hospitalarias

Este proyecto es una aplicación web desarrollada con Spring Boot para gestionar citas médicas en un hospital.

## Características

- Gestión de doctores
- Gestión de consultorios
- Gestión de citas

## Tecnologías utilizadas

- Java 24
- Gradle 23
- Spring Boot 3.4.5
- Spring Data JPA
- PostgreSQL
- Docker


## Configuración y ejecución

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd hospital
```

### 2. Iniciar la base de datos PostgreSQL con Docker

```bash
docker-compose up -d
```

### 3. Compilar y ejecutar la aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en: http://localhost:8080

## Datos de prueba

La aplicación carga automáticamente 5 doctores y 5 consultorios al iniciar, para facilitar las pruebas.