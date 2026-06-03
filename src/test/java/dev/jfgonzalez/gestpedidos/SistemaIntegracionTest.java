package dev.jfgonzalez.gestpedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SistemaIntegracionTest {

    @Test
    @DisplayName("CP-15: Prueba de Sistema End-to-End (E2E) Completa")
    void testFlujoEndToEndCompleto() {
        Tienda tienda = new Tienda();
        
        // GIVEN un cliente VIP de España
        Cliente cliente = new Cliente(1, "Carlos VIP", 3, true, "España");
        Pedido pedido = new Pedido(100, cliente);
        
        // AND un pedido con productos físicos y digitales
        ProductoFisico teclado = new ProductoFisico(1, "Teclado Mecanico", 100.0f, 1.5);
        ProductoFisico raton = new ProductoFisico(2, "Raton Gaming", 50.0f, 0.5);
        ProductoDigital licencia = new ProductoDigital(3, "Licencia SO", 120.0f, "1234-ABCD", 500, 0.21);
        
        pedido.addProducto(teclado, 1);
        pedido.addProducto(raton, 2);
        pedido.addProducto(licencia, 1);
        
        // WHEN el sistema procesa la venta
        Factura factura = tienda.realizarVenta(cliente, pedido);
        
        // THEN comprueba que la factura existe y tiene metadatos
        assertNotNull(factura, "La factura no debería ser nula");
        assertNotNull(factura.getCodigoFactura(), "El código de factura debe generarse");
        assertNotNull(factura.getFechaEmision(), "La fecha de emisión debe registrarse");
        
        // AND forzamos la cobertura del toString (CP-11/12) y JSON
        String resumen = factura.toString();
        assertTrue(resumen.contains("Total final:"), "El toString debe contener el desglose");
        
        // AND Identidad Aritmética matemática
        double calculoEsperado = factura.getTotalNeto() + factura.getTotalIva() + factura.getTotalEnvio() - factura.getDescuento();
        assertEquals(calculoEsperado, factura.getTotalFinal(), 0.01, "La factura debe mantener la identidad aritmética");
    }

    @Test
    @DisplayName("CP-16: Prueba de Robustez ante datos inválidos")
    void testRobustezDatosInvalidos() {
        // Validación 1: El sistema debe rechazar precios negativos al instanciar un producto
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductoFisico(99, "Producto Roto", -15.0f, 1.0);
        }, "Debería rechazar un precio negativo en el constructor");

        // Validación 2: Cliente con país vacío para forzar caída controlada
        Cliente clienteInvalido = new Cliente(2, "Fantasma", 0, false, "");
        Pedido pedido = new Pedido(101, clienteInvalido);
        pedido.addProducto(new ProductoDigital(4, "Juego", 60.0f), 1);
        
        Tienda tienda = new Tienda();
        assertThrows(IllegalArgumentException.class, () -> {
            tienda.realizarVenta(clienteInvalido, pedido);
        }, "Debería rechazar procesar una venta si el país del cliente está vacío");
    }

    @Test
    @DisplayName("CP-17: Integración Tienda-Cliente para cálculo de descuentos y getters")
    void testIntegracionDescuentosYCobertura() {
        Tienda tienda = new Tienda();
        // Cliente NO vip con 5 años de antigüedad en zona internacional (Francia)
        Cliente cliente = new Cliente(3, "Laura Veterana", 5, false, "Francia");
        Pedido pedido = new Pedido(102, cliente);
        
        // Producto genérico para cubrir instanciación base
        Producto camiseta = new ProductoFisico(5, "Merchandising", 100.0f);
        pedido.addProducto(camiseta, 1);
        
        Factura factura = tienda.realizarVenta(cliente, pedido);
        
        // Cobertura de getters y setters (CP-13)
        assertEquals("Laura Veterana", cliente.getNombre());
        assertEquals(5, cliente.getAnnosAntiguedad());
        assertEquals("Francia", cliente.getPais());
        assertFalse(cliente.getEsVip());
        assertEquals("Laura Veterana", pedido.getCliente().getNombre());
        
        // Verificamos que el envío europeo (5.0 base) se ha aplicado
        assertEquals(5.0, factura.getTotalEnvio(), 0.01);
    }
}