- [ ] Relación Pedido-Producto
Un Pedido contiene una colección de Productos. La integración falla si el pedido no sabe obtener el precio final de cada producto (con IVA/Envío).
- [ ] Relación Tienda-Cliente-Pedido
La Tienda depende de los datos del Cliente (para el descuento) y de los datos del Pedido (para la base imponible).
- [ ] Relación Tienda-Factura
La Tienda instancia una Factura. Los datos de la factura deben coincidir con la suma de los productos vendidos y el cliente que lo ha solicitado.