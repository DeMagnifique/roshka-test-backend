# Task Management API - Prueba Técnica Roshka

**Autor:** Danilo Rodriguez  
**Repositorio:** [roshka-test-backend](https://github.com/DeMagnifique/roshka-test-backend)

---

## Descripción del Proyecto

Esta aplicación API REST desarrollada como parte de la prueba técnica de Roshka. El proyecto cumple con todos los requisitos especificados, incluyendo CRUD completo, autenticación JWT y se siguieron buenas prácticas de desarrollo teniendo en cuenta los puntos de evaluación.

---

## Stack

| Tecnología              | Versión       | Motivo/Propósito             |
| ----------------------- | ------------- | ---------------------------- |
| **Java**                | 8+            | Lenguaje de programación     |
| **Spring Boot**         | 1.5.7.RELEASE | Framework principal          |
| **Spring Data JPA**     | Incluido      | Persistencia de datos        |
| **Spring Security**     | Incluido      | Autenticación y autorización |
| **H2 Database**         | Incluido      | Base de datos embebida       |
| **Hibernate Validator** | Incluido      | Validaciones                 |
| **JWT (jjwt)**          | 0.9.1         | Tokens de autenticación      |
| **Lombok**              | Incluido      | Reducción de boilerplate     |
| **Maven**               | 3.x           | Gestión de dependencias      |

---

## Instalación y Ejecución

### Prerrequisitos

- **Java 8 o superior** instalado
- **Maven 3.x** instalado
- **Git** para clonar el repositorio

### Pasos de Instalación

```bash
# 1. Clonar el repositorio
git clone https://github.com/DeMagnifique/roshka-test-backend.git
cd roshka-test-backend

# 2. Configurar variables de entorno
cp .env.example .env
# Editar .env con las configuraciones que se dejarán más abajo en este readme

# 3. Compilar y ejecutar
mvn clean install
mvn spring-boot:run
```

### Verificación

La aplicación estará disponible en:

- **API Base:** http://localhost:8080
- **H2 Console:** http://localhost:8080/h2-console

---

## Base de Datos H2

### Sin Configuración Externa Requerida

El proyecto incluye H2 Database embebida para simplificar la evaluación de esta prueba técnica. Por lo cual no es necesario ninguna configuración externa.

### Acceso a H2 Console

Para explorar los datos y ejecutar consultas SQL:

1. **URL:** http://localhost:8080/h2-console
2. **Configuración de conexión:**
   - **Driver Class:** `org.h2.Driver`
   - **JDBC URL:** `jdbc:h2:file:./data/taskdb`
   - **User Name:** `roshkatest`
   - **Password:** `roshkatest123`
3. **Click:** "Connect"

### Datos Precargados

El sistema incluye datos de ejemplo:

- **Rol Admin:** `admin` / `passwordadmin`
- **Rol User:** `user` / `userpass`
- **Tasks de ejemplo** con diferentes categorías
- **SubTasks** relacionadas
- **Timestamps** ideal para registrar el momento exacto de eventos y sincronizar datos entre diferentes zonas horarias - Muy importante en el sector bancario

---

## Autenticación

### Ejercicio 2 - JWT Implementation

El sistema implementa autenticación JWT con envío de tokens a web (vía cookies) y en el payload de la respuesta para mobile.

#### Para Aplicaciones Móviles

```bash
POST /login
Content-Type: application/json

{
  "username": "admin",
  "password": "passwordadmin"
}

# Respuesta: Token en payload
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

#### Para Aplicaciones Web

El mismo endpoint también envía el token como cookie HttpOnly para mayor seguridad web.

#### Endpoint Protegido

```bash
GET /profile
Authorization: Bearer <token>
```

## API Endpoints

## Ejercicio 1 – CRUD básico

### Tasks

| Método   | Endpoint      | Descripción              |
| -------- | ------------- | ------------------------ |
| `POST`   | `/tasks`      | Crear tarea              |
| `GET`    | `/tasks`      | Listar todas las tareas  |
| `GET`    | `/tasks/{id}` | Obtener una tarea por ID |
| `PUT`    | `/tasks/{id}` | Actualizar una tarea     |
| `DELETE` | `/tasks/{id}` | Eliminar una tarea       |

### SubTasks

| Método   | Endpoint                       | Descripción                 |
| -------- | ------------------------------ | --------------------------- |
| `POST`   | `/tasks/{id}/subtasks`         | Crear subtarea              |
| `GET`    | `/tasks/{id}/subtasks`         | Listar todas las subtareas  |
| `GET`    | `/tasks/{id}/subtasks/{subId}` | Obtener una subtarea por ID |
| `PUT`    | `/tasks/{id}/subtasks/{subId}` | Actualizar una subtarea     |
| `DELETE` | `/tasks/{id}/subtasks/{subId}` | Eliminar una subtarea       |

## Ejercicio 2 – Autenticación JWT

| Método | Endpoint   | Descripción                   |
| ------ | ---------- | ----------------------------- |
| `POST` | `/login`   | Obtener token JWT             |
| `GET`  | `/profile` | Datos del usuario autenticado |

---

## Paginación y Filtrado

### Query Parameters Soportados

```bash
GET /tasks?page=0&limit=10&sortBy=title&order=asc&category=DESARROLLO
GET /tasks/{id}/subtasks?page=0&limit=5&sortBy=createdAt&order=desc
```

| Parámetro  | Descripción          | Valores                      | Default |
| ---------- | -------------------- | ---------------------------- | ------- |
| `page`     | Número de página     | 0, 1, 2...                   | 0       |
| `limit`    | Elementos por página | 1, 5, 10, 20...              | 10      |
| `sortBy`   | Campo para ordenar   | id, title, createdAt...      | id      |
| `order`    | Dirección del orden  | asc, desc                    | asc     |
| `category` | Filtro por categoría | DESARROLLO, QA, SEGURIDAD... | (todos) |

### Estructura de Respuesta

```json
{
  "data": {
    "content": [...],
    "totalElements": 25,
    "totalPages": 3,
    "size": 10,
    "number": 0
  },
  "meta": {
    "total": 25,
    "page": 0,
    "totalPages": 3
  }
}
```

## Configuración de Seguridad

### Variables de Entorno (.env)

El proyecto utiliza variables de entorno para gestionar secretos:

```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar manualmente el .env
SPRING_DATASOURCE_URL=jdbc:h2:file:./data/taskdb
SPRING_DATASOURCE_USERNAME=roshkatest
SPRING_DATASOURCE_PASSWORD=roshkatest123!

SPRING_H2_CONSOLE_ENABLED=true
SPRING_H2_CONSOLE_PATH=/h2-console

JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXPIRATION=86400000

SERVER_PORT=8080

SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
SPRING_JPA_SHOW_SQL=true
```

## Mi test QA lo he realizado siguiendo estos pasos.

### He decidido empezar por Autenticación JWT al ser más sencillo y rápido.

### Adjuntaré mis CURLs listos para copiarse, estos pueden ser pegados en postman como interpreter y ejecutados. Sugiero cambiar los datos si así lo desean, pues, yo he testeado unas cuantas veces los 12 endpoints y en todos los casos son de éexito!

#### 1. Obtener token de usuario. POST /login

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data '{
  "username": "admin",
  "password": "passwordadmin"
}'
```

#### 2. Solicitar información del usuario. GET /profile

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/profile' \
--header 'Authorization: Bearer {token}'
```

### Ahora pasamos al CRUD

#### 1. Listar todas las tareas. GET /tasks

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/tasks'
```

#### 2. Crear tarea. POST /tasks

**Respuesta esperada: HTTP 201 Created**

```bash
curl --location 'localhost:8080/tasks' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Enviar a Roshka el repositorio GIT",
    "description": "Damos retorno a los compañeros de roshka sobre los servicios solicitados",
    "category": "ENTREGA"
}'
```

#### 3. Obtener una tarea por ID. GET /tasks/:id

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/tasks/6'
```

#### 4. Actualizar una tarea. PUT /tasks/:id

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location --request PUT 'localhost:8080/tasks/6' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Enviar a Roshka el repositorio GIT",
    "description": "Remitimos a los compañeros de roshka sobre los servicios solicitados",
    "category": "ENTREGA"
}'
```

#### 5. Eliminar una tarea. DELETE /tasks/:id

**Respuesta esperada: HTTP 204 No Content**

```bash
curl --location --request DELETE 'localhost:8080/tasks/2'
```

#### 6. Crear subtarea. POST /tasks/:id/subtasks

**Respuesta esperada: HTTP 201 Created**

```bash
curl --location 'localhost:8080/tasks/6/subtasks' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Responder el correo",
    "description": "TO: aflorentin; CC: Mati y Mario"
}'
```

#### 7. Listar todas las subtareas. GET /tasks/:id/subtasks

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/tasks/6/subtasks'
```

#### 8. Obtener una subtarea por ID. GET /tasks/:id/subtasks/:id

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location 'localhost:8080/tasks/6/subtasks/7'
```

#### 9. Actualizar una subtarea. PUT /tasks/:id/subtasks/:id

**Respuesta esperada: HTTP 200 OK**

```bash
curl --location --request PUT 'localhost:8080/tasks/6/subtasks/7' \
--header 'Content-Type: application/json' \
--data '{
    "title": "Responder el correo",
    "description": "TO: aflorentin; CC: movillalba, morue"
}'
```

#### 10. Eliminar una subtarea. DELETE /tasks/:id/subtasks/:id

**Respuesta esperada: HTTP 204 No Content**

```bash
curl --location --request DELETE 'localhost:8080/tasks/6/subtasks/7'
```
