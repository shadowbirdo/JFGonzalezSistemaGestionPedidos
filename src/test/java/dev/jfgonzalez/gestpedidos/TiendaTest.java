package dev.jfgonzalez.gestpedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TiendaTest {
    @Test
    @DisplayName("Test de cobertura para excepciones")
    void testTiendaExcepciones() {
        Tienda tienda = new Tienda();
        Cliente clienteEspana = new Cliente(1, "Socio", 0, false, "España");
        Cliente clienteFrancia = new Cliente(2, "Voisin", 0, false, "Francia");
        Pedido pedidoEspana = new Pedido(1, clienteEspana);

        assertThrows(IllegalArgumentException.class, () -> {
            tienda.realizarVenta(clienteFrancia, pedidoEspana);
        },"Si el cliente no coincide con el pedido debería saltar una excepción");

        Cliente clienteSinPais = new Cliente(3, "Indefinido", 0, false, "   ");
        Pedido pedidoSinPais = new Pedido(2, clienteSinPais);
        pedidoSinPais.addProducto(new ProductoDigital(10, "Software", 50.0), 1);
        assertThrows(IllegalArgumentException.class, () -> {
            tienda.realizarVenta(clienteSinPais, pedidoSinPais);
        },"Si no hay país en cliente o pedido, debería saltar una excepción");


    }

    @Test
    @DisplayName("Test de cobertura para flujos de negocio alternativos")
    void testTiendaEnviosAlternativos(){
        Tienda tienda = new Tienda();
        Cliente clienteEspana = new Cliente(1, "Socio", 0, false, "España");
        Cliente clienteFrancia = new Cliente(2, "Voisin", 0, false, "Francia");
        Cliente clienteSinPais = new Cliente(3, "Indefinido", 0, false, "   ");
        Pedido pedidoDigital = new Pedido(3, clienteEspana);
        Pedido pedidoGenerico = new Pedido(4, clienteFrancia);
        Pedido pedidoSinPais = new Pedido(2, clienteSinPais);
        
        pedidoDigital.addProducto(new ProductoDigital(20, "Ebook", 10.0), 1);
        Factura facturaDigital = tienda.realizarVenta(clienteEspana, pedidoDigital);
        pedidoGenerico.addProducto(new Producto(30, "Lote Base", 20.0), 1);
        Factura facturaGenerico = tienda.realizarVenta(clienteFrancia, pedidoGenerico);
        pedidoSinPais.addProducto(new ProductoDigital(10, "Software", 50.0), 1);
       
        assertEquals(0.0, facturaDigital.getTotalEnvio(), 0.01);
        assertEquals(0.0, facturaGenerico.getTotalEnvio(), 0.01);
    }
}
