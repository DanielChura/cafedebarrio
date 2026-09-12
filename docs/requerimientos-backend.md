# Requerimientos Backend + BD — Café de Barrio

## 1. Alcance
Catálogo, pedidos con descuento de stock, admin básica. Carrito es temporal en frontend, backend solo recibe pedido confirmado.

## 2. Funcionales (backend)
- RF1 `GET /api/productos` + `?categoria={id}` + `?soloActivos=true` — lista catálogo.
- RF2 `GET /api/productos/{id}` — detalle.
- RF3 `POST /api/productos` / `PUT /api/productos/{id}` — crear/editar (nombre, precio>0, stock>=0, categoria).
- RF4 `DELETE /api/productos/{id}` — baja lógica (`activo=false`).
- RF5 `POST /api/pedidos` — crea pedido: requiere `clienteNombre, celular, direccion, items[productoId, cantidad>=1]`. Valida stock, calcula `total`, descuenta stock en una sola transacción. Si falta stock → `400`.
- RF6 `GET /api/pedidos` — lista con estado + total.
- RF7 `PATCH /api/pedidos/{id}/estado` — solo `PENDIENTE → EN_PREPARACION → ENTREGADO`.
- RF8 Errores estándar `{ message, status, errors? }`.

## 3. Base de datos (SQL Server)
- `Categoria(id PK, nombre UNIQUE NOT NULL)`
- `Producto(id PK, nombre NOT NULL, descripcion, precio DECIMAL(10,2)>0, stock INT>=0, imagenUrl, activo BIT DEFAULT 1, categoriaId FK → Categoria)`
- `Pedido(id PK, clienteNombre NOT NULL, celular NOT NULL, direccion NOT NULL, fecha DATETIME DEFAULT GETDATE(), estado VARCHAR(20) DEFAULT 'PENDIENTE', total DECIMAL(10,2))`
- `DetallePedido(id PK, pedidoId FK → Pedido, productoId FK → Producto, cantidad>0, precioUnitario, subtotal)`
- Reglas: `total = SUM(subtotal)` calculado en backend. Stock nunca negativo. `ddl-auto=update` en dev.

## 4. Aceptación
1. Catálogo carga desde API real.
2. Pedido sin datos o sin items → `400`.
3. Pedido sin stock → `400` + mensaje, stock intacto.
4. Pedido válido → `201` y stock disminuye.
5. Producto creado en admin aparece en catálogo.
