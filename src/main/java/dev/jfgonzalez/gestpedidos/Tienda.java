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
        if (pedido.getCliente() != cliente) throw new IllegalArgumentException("El cliente no coincide");
        if (cliente.getPais() == null || cliente.getPais().isBlank() || cliente.getPais().isEmpty()) throw new IllegalArgumentException("El atributo país está vacío");
        if (pedido.getProductos().isEmpty()) throw new IllegalArgumentException(Pedido.PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE);

        double totalNeto = 0;
        for (Producto p : pedido.getProductos()) {
            int cantidad = pedido.getCantidades().getOrDefault(p.getId(), 1);
            totalNeto += p.getPrecioBase() * cantidad;
        }
        
        boolean soloDigitales = pedido.getProductos().stream().allMatch(p -> p instanceof ProductoDigital);
        double totalEnvio = soloDigitales ? 0 : pedido.calcularEnvio(cliente.getPais());
        boolean tieneFisicos = pedido.getProductos().stream().anyMatch(p -> p instanceof ProductoFisico);
        if (!tieneFisicos) {
            totalEnvio = 0;
        }

        double totalIva = pedido.calcularIva("GENERAL");

        double totalBruto = totalNeto + totalIva + totalEnvio;

        double descuentoPorcentaje = calcularDescuento(cliente);

        double descuento = totalBruto * descuentoPorcentaje;
        double totalFinal = totalBruto - descuento;

        Factura factura = new Factura();
        factura.setCodigoFactura("FACT-" + java.time.LocalDate.now() + "-" + System.nanoTime());
        factura.setFechaEmision(java.time.LocalDate.now());
        
        factura.setTotalNeto(totalNeto);
        factura.setTotalIva(totalIva);
        factura.setTotalEnvio(totalEnvio);
        factura.setDescuento(descuento);
        factura.setTotalFinal(totalFinal);
        
        return factura;
    }

}
