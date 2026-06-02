package dev.jfgonzalez.gestpedidos.service;

import dev.jfgonzalez.gestpedidos.model.*;

/**
 * - Método realizarVenta(Cliente c, Pedido p): Debe ser el punto de entrada que orqueste todo el flujo.
 * - Cálculo de Descuentos: Aplica la lógica de fidelidad basada en el objeto Cliente sobre el total devuelto por el objeto Pedido.
 * - Generación: Si todo es correcto, debe instanciar y devolver un objeto Factura.
 * - processSale
 */
public class Tienda {

    public Factura realizarVenta(Cliente cliente, Pedido pedido) {
        if (pedido.getCliente() != cliente) throw new IllegalArgumentException();
        if (!pedido.getProductos().stream().anyMatch(p -> p instanceof ProductoDigital)) {
            /* Cálculo para productos físicos */
        }
        double totalNeto = 0;
        for (Producto p : pedido.getProductos()) {
            totalNeto += p.getPrecioBase();
        }
        Factura factura = new Factura();
        
        return factura;
    }

}
