package dev.jfgonzalez.gestpedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FacturaTest {

    @Test
    @DisplayName("Cobertura: método toJson genera el formato JSON esperado")
    void testFacturaToJson() {
        Factura factura = new Factura();
        factura.setCodigoFactura("JSON-999");
        factura.setFechaEmision(LocalDate.now());
        factura.setTotalNeto(100.0);
        factura.setTotalIva(21.0);
        factura.setTotalEnvio(5.0);
        factura.setDescuento(10.0);
        factura.setTotalFinal(116.0);

        String json = factura.toJson();

        assertNotNull(json, "El JSON no debería ser nulo");
        assertTrue(json.startsWith("{") && json.endsWith("}"), "Debe estar envuelto en llaves");
        assertTrue(json.contains("\"codigoFactura\":\"JSON-999\""), "Debe contener el código de factura");
        assertTrue(json.contains("totalNeto"), "Debe contener las claves de los totales");
    }

    @Test
    @DisplayName("Cobertura: calcularFactura procesa el pedido y resetea variables")
    void testCalcularFactura() {
        Cliente cliente = new Cliente(99, "Cliente Test", 0, false, "España");
        Pedido pedido = new Pedido(99, cliente);
        pedido.addProducto(new ProductoFisico(10, "Cascos", 50.0, 1.0), 2); 

        Factura factura = new Factura();
        factura.calcularFactura(pedido);

        assertEquals(100.0, factura.getTotalNeto(), 0.01, "El total neto debería ser 100");
        assertEquals(0.0, factura.getTotalIva(), "El IVA debería resetearse a 0");
        assertEquals(0.0, factura.getTotalEnvio(), "El envío debería resetearse a 0");
        assertEquals(0.0, factura.getDescuento(), "El descuento debería resetearse a 0");
        assertEquals(0.0, factura.getTotalFinal(), "El total final debería resetearse a 0");
    }

    @Test
    @DisplayName("Cobertura: generarFactura devuelve el texto formateado correctamente")
    void testGenerarFactura() {
        Cliente cliente = new Cliente(88, "Cliente Ticket", 0, false, "España");
        Pedido pedido = new Pedido(88, cliente);
        pedido.addProducto(new ProductoDigital(11, "Juego Digital", 60.0), 1);

        Factura factura = new Factura();
        factura.setCodigoFactura("TICKET-001");
        
        String ticket = factura.generarFactura(pedido);

        assertNotNull(ticket, "El texto de la factura no debe ser nulo");
        assertTrue(ticket.contains("TICKET-001"), "Debe contener el código de factura");
        assertTrue(ticket.contains("Juego Digital"), "Debe incluir el nombre de los productos");
        assertTrue(ticket.contains("Total neto:"), "Debe contener los apartados de desglose");
    }

    @Test
    @DisplayName("Test de cobertura para constructor sobrecargado con floats")
    void testFacturaConstructorFloat() {
        // Invoca directamente al constructor alternativo float para pintar la barra roja de verde
        Factura factura = new Factura(80.0f, 16.8f, 5.0f, 101.8f);
        
        assertNotNull(factura);
        assertEquals(80.0, factura.getTotalNeto(), 0.01);
        assertEquals(16.8, factura.getTotalIva(), 0.01);
        assertEquals(5.0, factura.getTotalEnvio(), 0.01);
        assertEquals(101.8, factura.getTotalFinal(), 0.01);
    }
}