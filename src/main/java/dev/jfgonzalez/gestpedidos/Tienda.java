package dev.jfgonzalez.gestpedidos;

/**
 * - Método realizarVenta(Cliente c, Pedido p): Debe ser el punto de entrada que orqueste todo el flujo.
 * - Cálculo de Descuentos: Aplica la lógica de fidelidad basada en el objeto Cliente sobre el total devuelto por el objeto Pedido.
 * - Generación: Si todo es correcto, debe instanciar y devolver un objeto Factura.
 * - processSale
 */
public class Tienda {
    // Constantes
    public static final double DESCUENTO_VIP = .05;
    public static final double DESCUENTO_ANTIGUEDAD = .05;
    
    // Métodos
    /**
     * Calcula el porcentaje de descuento que se aplicará a la compra según los años de antigüedad y el tipo de membresía del cliente.
     * @param cliente - Cliente para el que se usará el descuento
     * @return El porcentaje de descuento a utilizar
     */
    public double calcularDescuento(Cliente cliente) {
        return (cliente.getEsVip() ? DESCUENTO_VIP : 0) + (cliente.getAnnosAntiguedad() > 5 ? DESCUENTO_ANTIGUEDAD : 0);
    }

    /**
     * Genera una factura sobre el pedido introducido, comprobando también los datos del cliente.
     * @param cliente - Cliente que hace la compra
     * @param pedido - Pedido que se va a pagar
     * @return Factura resultante
     */
    public Factura realizarVenta(Cliente cliente, Pedido pedido) {
        if (pedido.getCliente() != cliente) throw new IllegalArgumentException();

        if (cliente.getPais() == null || cliente.getPais().isBlank() || cliente.getPais().isEmpty()) throw new IllegalArgumentException();

        if (!pedido.getProductos().stream().anyMatch(p -> p instanceof ProductoDigital)) {
            /* Cálculo para productos físicos */
        }
        double totalFinal = 0;
        double totalNeto = 0;
        for (Producto p : pedido.getProductos()) {
            totalNeto += p.getPrecioBase();
        }
        Factura factura = new Factura();
        factura.setTotalNeto(totalNeto);
        factura.setTotalFinal(totalFinal);
        factura.setDescuento(pedido.calcularTotal() * (1 - calcularDescuento(cliente)));
        factura.generarFactura(pedido);
        
        return factura;
    }

}
