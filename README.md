# ForoHub API
# Desarrollado por: Mariana Andrea Lois (Lady Marianne).

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
- **GET /topics**: Listar todos los tópicos activos.
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

### Integración con OpenAI usando SpringAI para moderar contenido:

En el proyecto ForoHub, hemos implementado una integración con OpenAI para moderar los contenidos 
generados por los usuarios antes de que se almacenen en la base de datos. Este sistema asegura que los 
tópicos creados cumplan con las reglas del foro, promoviendo un ambiente respetuoso y relevante para 
todos los participantes.

#### Objetivo de la moderación:
El objetivo principal de esta funcionalidad es validar que:

1. **No se publiquen contenidos discriminatorios, ofensivos o insultantes**: Por ejemplo, mensajes que 
fomenten estereotipos o ataquen a personas por su género, etnia, religión u otras características 
personales.
2. **El contenido sea relevante para las temáticas del foro**: ForoHub está enfocado en temas 
relacionados con desarrollo backend y frontend, DevOps, bases de datos e ingeniería de software. 
Mensajes fuera de estas áreas serán rechazados.

#### Cómo funciona la integración:
1. **Creación del prompt**: Cuando un usuario intenta registrar un tópico, el sistema genera un prompt
que describe las reglas del foro y presenta el contenido del tópico (título y mensaje) como texto a
evaluar.

2. **Consulta al modelo de OpenAI**: El servicio `ModerationService` envía el prompt al modelo de
lenguaje de OpenAI utilizando la dependencia `SpringAI` para realizar una solicitud al API.

3. **Evaluación del contenido**: OpenAI responde con un veredicto: “CUMPLE” o “NO CUMPLE”, indicando 
si el contenido respeta las reglas establecidas.

4. **Decisión basada en el resultado**:
    - Si el contenido “CUMPLE” con las reglas, el estado del tópico cambia a **ACTIVE** (activo).
    - Si el contenido “NO CUMPLE”, el estado del tópico cambia a **DELETED** (eliminado) para evitar 
   su publicación.

#### Código involucrado:

1. **Clase `ModerationService`**: Contiene la lógica principal para interactuar con el modelo de OpenAI,
enviar el prompt y procesar la respuesta.

2. **Clase `OpenAIService`**: Define la configuración y la comunicación directa con la API de OpenAI, 
utilizando el modelo `gpt-4o-mini` y configurando los parámetros necesarios para cada solicitud.

3. **Clase `TopicController`**: Invoca la moderación desde el endpoint de creación de tópicos 
(`registerTopic`) para determinar si el contenido es aceptable.

#### Beneficios de la integración:

- **Automatización**: La moderación automática ahorra tiempo al equipo administrativo del foro.
- **Consistencia**: Se aplica un conjunto uniforme de reglas a todos los tópicos.
- **Prevención proactiva**: Los contenidos inapropiados no llegan a ser publicados, manteniendo la 
calidad del foro.

#### Mejoras futuras:
Aunque la integración actual es funcional, planeamos mejorarla implementando:
- **Retroalimentación al usuario**: Brindar mensajes claros sobre qué reglas fueron violadas en caso
de que el contenido no cumpla.
- **Moderación de respuestas**: Extender la funcionalidad para validar también los mensajes publicados
como respuestas.

Con esta implementación, ForoHub se asegura de ofrecer un espacio seguro y útil para su comunidad técnica.

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