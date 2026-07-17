# InstaFlux Backend 🚀

InstaFlux es el backend de una red social (estilo Instagram) construida con principios de alto rendimiento, 
diseñada para ser completamente asíncrona, no bloqueante y altamente escalable.

## 🏗️ Arquitectura y Tecnologías

El proyecto está construido utilizando Arquitectura Hexagonal (Ports and Adapters), lo que permite un alto nivel de 
desacoplamiento entre la lógica de negocio (Dominio/Casos de uso) y los detalles técnicos (Bases de datos, Frameworks web).

- Framework: Java 17+ y Spring Boot 3.
- Paradigma: Programación Reactiva con Spring WebFlux (Project Reactor: Mono y Flux).
- Base de Datos: MongoDB (utilizando Spring Data Reactive MongoDB).
- Seguridad: Autenticación Stateless con JWT (JSON Web Tokens) y filtros reactivos.
- Tiempo Real: Spring WebSockets (comunicación bidireccional reactiva usando Sinks).
- Contenedores: Docker y Docker Compose para infraestructura local.

## 🛠️ Cómo ejecutar el proyecto localmente

1. Requisitos Previos

- Tener Docker y Docker Compose instalados.
- Tener Java 17 o superior.
- (Opcional pero recomendado) Postman para probar los endpoints y WebSockets.

2. Levantar la Base de Datos

- Abre una terminal en la raíz del proyecto y ejecuta:

```Bash
docker-compose up -d
```

- Esto descargará y ejecutará MongoDB con las credenciales configuradas en el docker-compose.yml.

3. Ejecutar la Aplicación

- Desde tu IDE (VS Code, IntelliJ) corre la clase principal InstafluxApplication.java, o usa la terminal:

```Bash
./gradlew bootRun
```

La aplicación se levantará en el puerto [http://localhost:8080](http://localhost:8080).

## 📚 Documentación de la API (REST & WebSockets)

Todas las respuestas REST siguen este estándar (ResponseGlobal):

```Bash
{
  "success": true,
  "status": 200,
  "message": "Mensaje de éxito",
  "data": { ... },
  "timestamp": "2026-07-17T12:00:00Z"
}
```

### 🔐 1. Autenticación (Públicos)

| Método | Endpoint | Descripción | Body (JSON) |
| --- | --- | --- | --- |
| POST | /api/auth/register | Crea un nuevo usuario. | username, email, password |
| POST | /api/auth/login | Inicia sesión y devuelve token JWT. | email, password |

(Nota: Para todos los endpoints a continuación, debes incluir el token en los headers: Authorization: Bearer <TU_TOKEN>).

### 👤 2. Usuarios y Perfil

| Método | Endpoint | Descripción | Body (JSON) |
| --- | --- | --- | --- |
| GET | /api/users/me | Obtiene el perfil del usuario autenticado. | - |
| GET | /api/users/{email} | Obtiene el perfil público de otro usuario. | - |
| PATCH | /api/users/me/bio | Actualiza la biografía del usuario logueado. | Nuevabiografía |

### 🤝 3. Seguidores (Followers)

| Método | Endpoint | Descripción |
| --- | --- | --- |
| POST | /api/users/{targetEmail}/follow | Seguir a un usuario. |
| DELETE | /api/users/{targetEmail}/follow | Dejar de seguir a un usuario. |

### 📸 4. Posts y Feed

| Método | Endpoint | Descripción |
| --- | --- | --- |
| POST | /api/posts | Crear un nuevo Post subiendo una imagen. |
| GET | /api/posts | Obtener el Feed Personalizado. |
| GET | /api/posts/user/{email} | Obtener todos los posts de un autor específico. |
| POST | /api/posts/{postId}/like | Dar "like" a un post. |

### 💬 5. Chat en Tiempo Real (WebSockets Reactivos)

El chat utiliza conexiones bidireccionales y Sinks.Many en memoria para el ruteo de mensajes sin bloqueo.

Conexión inicial:
Debes conectarte enviando el JWT por URL (ya que los navegadores no permiten Custom Headers nativos en WebSockets).

URL: [ws://localhost:8080/ws/chat?token=TU_JWT_TOKEN](ws://localhost:8080/ws/chat?token=TU_JWT_TOKEN)

Enviar un mensaje (Petición desde el Cliente):
Una vez conectada la sesión WebSocket, envía un mensaje en texto plano formateado como JSON:

```Bash
{
  "receiverEmail": "amigo@example.com",
  "content": "¡Hola, mundo asíncrono!"
}
```

Recibir un mensaje (Respuesta hacia el Cliente):
Si estás conectado y alguien te envía un mensaje, el servidor hará un push automáticamente a tu cliente WebSocket con el siguiente formato:

```Bash
{
  "id": "60a5b...",
  "senderEmail": "otro@example.com",
  "receiverEmail": "tu_correo@example.com",
  "content": "¡Hola, mundo asíncrono!",
  "timestamp": "2026-07-17T15:30:00Z"
}
```

Construido con ❤️ usando Spring WebFlux y Arquitectura Hexagonal.
Realizado por Daniel Singer ZER0SEV7N.