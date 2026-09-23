# TechStore Pro — Backend

API del mini e-commerce de accesorios tecnológicos. Java 21 + Spring Boot + JWT + SQL Server.

## Requisitos

- Java 21 (no necesitas instalar Maven, el wrapper lo descarga solo la primera vez).
- SQL Server local con la base de datos `techstorepro` creada.
- Credenciales de Cloudinary (las imágenes de producto son requeridas, solo WEBP)
  y de Google OAuth2 (login con Google).

## Configuración

```bash
copy .env.example .env   # Git Bash: cp .env.example .env
```

Completa los valores en `.env` (ese archivo no se commitea).

## Ejecución

```bash
mvnw.cmd spring-boot:run   # Git Bash: ./mvnw spring-boot:run
```

La primera vez descarga Maven automáticamente (requiere internet).
Alternativa sin terminal: abre el proyecto en IntelliJ o VS Code y ejecuta la aplicación.
App: `http://localhost:8080` — con `ddl-auto=update` las tablas se crean solas.

## API

Todos los endpoints están documentados en Swagger UI:
`http://localhost:8080/swagger-ui.html`

- `POST /api/auth/register` y `POST /api/auth/login` son públicos y devuelven el JWT
  (login inválido → `401`).
- El resto de escritura y `/api/auth/me` requieren header
  `Authorization: Bearer <token>`. El usuario siempre sale del token.

## Reglas de negocio

- El total y el stock se calculan/validan en backend; el pedido descuenta stock
  en una sola transacción (sin stock → `400`).
- Pedido: `PENDING → PREPARING → DELIVERED`, solo ADMIN y sin saltos.
- Productos con baja lógica (`activo=false`); reviews solo de quien compró.
- Error estándar `{ message, status, errors? }`, sin stacktraces.
