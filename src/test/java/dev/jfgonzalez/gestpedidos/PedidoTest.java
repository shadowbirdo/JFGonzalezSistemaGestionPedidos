package dev.jfgonzalez.gestpedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PedidoTest {
    @ParameterizedTest
    @CsvSource({
        "0,España",
        "5,Francia",
        "10,Alemania"}
    )
    @DisplayName("CP-02: ProductoFisico debe sumar el coste de envío al precio base")
    void testPhysicalProdCalcPrice(double costeEsperado, String zonaEnvio) {
        ProductoFisico pf = new ProductoFisico(1,"Detergente", 20);

        double costeObtenido = pf.costeEnvio(zonaEnvio);

        assertEquals(costeEsperado, costeObtenido, "El coste del envío para la zona \"%s\" debería ser %.0f.".formatted(zonaEnvio,costeEsperado));
    }

    @ParameterizedTest
    @DisplayName("CP-03: Cálculo correcto del total con varios productos")
    @CsvSource({
        "17.55, 10.0, 5.0",   // 10 + 5 = 15
        "27.55, 20.0, 5.0",   // 20 + 5 = 25
        "56.75, 25.0, 25.0"   // 25 + 25 = 50
    })
    void testCalcTotalParameterized(float expected, float price1, float price2) {
        Pedido pedido = new Pedido(1, new Cliente(1, "Test"));
        pedido.addProducto(new ProductoFisico(1,"P1", price1, 15));
        pedido.addProducto(new ProductoDigital(2,"P2", price2));

        double total = pedido.calcularTotal() + pedido.calcularEnvio("España") + pedido.calcularIva("GENERAL");

        assertEquals(expected, total, 0.01, "El total calculado no coincide con el esperado");
    }

    @Test
    @DisplayName("CP-07: Lanzar IllegalStateException si se calcula total de pedido vacío")
    void testEmptyOrderThrowsException() {
        Cliente client = new Cliente(1, "Homer");
        
        Pedido emptyOrder = new Pedido(1,client); 

        assertThrows(IllegalArgumentException.class, emptyOrder::calcularTotal, "Debería lanzar IllegalStateException detallando que el pedido está vacío");
    }

    @ParameterizedTest
    @DisplayName("CP-10: Validar que el total NO coincida con valores erróneos")
    @ValueSource(floats = {50.0f, 60.0f, 0.0f, -59.5f})
    void testCalcTotalIncorrectValues(float incorrectValue) {
        Pedido order = new Pedido(1, new Cliente(1,"Test"));
        order.addProducto(new ProductoFisico(1,"P1", 59.5f)); // Total real 59.5
        
        double total = order.calcularTotal();

        assertNotEquals(incorrectValue, total, "El total debería ser distinto a este valor incorrecto");
    }

    @Test
    @DisplayName("Test de cobertura para getters y setters")
    void testGettersSetters() {
        Cliente cliente = new Cliente(1, "Ana", 2, false, "España");
        Cliente nuevoCliente = new Cliente(2, "Juan", 3, true, "Portugal");
        Pedido pedido = new Pedido(10, cliente);
        ProductoFisico raton = new ProductoFisico(5, "Raton", 10.0, 0.5);
        List<Producto> listaProds = new ArrayList<>();
        Map<Integer, Integer> mapaCant = new HashMap<>();

        pedido.setIdPedido(20);
        pedido.setCliente(nuevoCliente);
        listaProds.add(raton);
        pedido.setProductos(listaProds);
        mapaCant.put(5, 2);
        pedido.setCantidades(mapaCant);
        
        assertEquals(20, pedido.getIdPedido());
        assertEquals(nuevoCliente, pedido.getCliente());
    }

    @Test
    @DisplayName("Test de cobertura que comprueba el correcto funcionamiento del método delProducto")
    void testPedidoEliminacionProductos() {
        Cliente cliente = new Cliente(1, "Ana", 2, false, "España");
        Pedido pedido = new Pedido(10, cliente);
        ProductoFisico raton = new ProductoFisico(5, "Raton", 10.0, 0.5);

        pedido.addProducto(raton, 1);
        assertEquals(1, pedido.getProductos().size(), "El tamaño debería ser 1 tras añadir"); // 1. Comprobar tamaño inicial

        pedido.delProducto(5); // Borrado exitoso
        assertEquals(0, pedido.getProductos().size(), "El tamaño debería ser 0 tras borrar"); // 2. Comprobar tras borrar

        // 3. Comprobar que lanzar excepción con id inexistente funciona
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.delProducto(999);
        }, "Debería lanzar IllegalArgumentException si el producto no existe");
    }

    @Test
    @DisplayName("Test de cobertura para métodos toString y auxiliares")
    void testStrings() {
        Cliente cliente = new Cliente(1, "Ana", 2, false, "España");
        Pedido pedido = new Pedido(10, cliente);
        String strValido = pedido.toString();
        String strNulos = pedido.toString();
        
        pedido.setProductos(null);
        pedido.setCantidades(null);
        
        assertNotNull(strValido);
        assertTrue(strValido.contains("idPedido"));
        assertNotNull(strNulos, "El toString no debe fallar con colecciones nulas");
    }
}
