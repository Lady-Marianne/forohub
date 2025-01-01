# ForoHub API

ForoHub es un foro para discutir sobre diferentes tópicos relacionados con el desarrollo BackEnd.
Los usuarios pueden crear tópicos, responder a ellos, y editar sus respuestas dentro de un marco de 
reglas de tiempo específicas. La API está construida con **Spring Boot** 
y utiliza JWT para su autenticación.

## Descripción:

Esta aplicación es un foro donde los usuarios pueden:
- Crear y gestionar tópicos.
- Responder a los tópicos existentes.
- Editar sus respuestas dentro de una hora desde la publicación.
- Respetar restricciones de horario para realizar publicaciones (7:00 AM a 11:59 PM).

La API está protegida mediante autenticación basada en JWT.

## Funcionalidades:

### Tópicos:
- **POST /topics**: Crear un nuevo tópico.
- **GET /topics**: Listar todos los tópicos.
- **GET /topics/{id}**: Obtener detalles de un tópico específico.
- **PUT /topics/{id}**: Editar un tópico.
- **DELETE /topics/{id}**: Eliminar un tópico.

### Respuestas:
- **POST /answers**: Crear una respuesta a un tópico.
- **GET /answers/{topicId}**: Obtener respuestas de un tópico.
- **PUT /answers/{id}**: Editar una respuesta.
- **DELETE /answers/{id}**: Eliminar una respuesta.

### Autenticación:
- **POST /login**: Iniciar sesión y obtener un token JWT.
- **POST /users**: Crear un nuevo usuario.

### Reglas de Negocio:
1. **Restricciones de horario para publicaciones**: Sólo se pueden hacer publicaciones entre las 7:00 AM 
2. y las 11:59 PM.
2. **Tiempo para editar tópicos**: Los tópicos sólo se pueden editar dentro de la primera hora 
después de su creación.

## Tecnologías:

- **Backend**: Spring Boot 2.x, Spring Security, JPA, JWT.
- **Base de datos**: MySQL.
- **Autenticación**: JWT (JSON Web Token).
- **Testing**: JUnit, Mockito. (Próximamente)

## Requisitos:

- Java 17 o superior.
- Maven.
- MySQL (o cualquier base de datos compatible con JPA).
- Instalar dependencias con:
  ```bash
  mvn install
  ```

## Instalación:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu_usuario/forohub.git
   ```

2. Configura tu base de datos y agrega las credenciales en el archivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/forohub
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Corre la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints:

### Autenticación:

#### POST /login:
Inicia sesión y recibe un token JWT.

**Request Body**:
```json
{
  "username": "usuario",
  "password": "contraseña"
}
```

**Response**:
```json
{
  "jwTtoken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### POST /users:
Crea un nuevo usuario.

**Request Body**:
```json
{
  "username": "nuevo_usuario",
  "password": "contraseña",
  "email": "email@dominio.com"
}
```

**Response**:
```json
{
  "message": "Usuario creado exitosamente"
}
```

### Tópicos:

#### POST /topics:
Crea un nuevo tópico.

**Request Body**:
```json
{
  "title": "Título del tópico",
  "message": "Contenido del mensaje",
  "author": "Nombre del autor"
}
```

**Response**:
```json
{
  "id": 1,
  "title": "Título del tópico",
  "message": "Contenido del mensaje",
  "author": "Nombre del autor",
  "created_at": "2025-01-01T10:00:00"
}
```

#### GET /topics:
Obtiene todos los tópicos.

**Response**:
```json
[
  {
    "id": 1,
    "title": "Título del tópico",
    "message": "Contenido del mensaje",
    "author": "Nombre del autor",
    "created_at": "2025-01-01T10:00:00"
  }
]
```

### Respuestas:

#### POST /answers:
Crea una respuesta a un tópico.

**Request Body**:
```json
{
  "topicId": 1,
  "message": "Contenido de la respuesta"
}
```
## Seguridad:

La API utiliza **JWT (JSON Web Token)** para la autenticación. Debes incluir el token en el encabezado
de autorización de las solicitudes:

```bash
Authorization: Bearer <tu_token_jwt>
```

## Documentación:

La documentación de la API está disponible en Swagger UI:

- **URL**: `/swagger-ui.html`