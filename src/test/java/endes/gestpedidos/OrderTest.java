package endes.gestpedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {
    @ParameterizedTest
    @DisplayName("CP-03: Cálculo correcto del total con varios productos")
    @CsvSource({
        "14.75, 10.0, 5.0",   // 10 + 5 = 15
        "24.75, 20.0, 5.0",   // 20 + 5 = 25
        "48.75, 25.0, 25.0"   // 25 + 25 = 50
    })
    void testCalcTotalParameterized(float expected, float p1, float p2) {
        Order order = new Order(new Client("Test", "test@test.es", "Calle"));
        order.addProduct(new PhysicalProd("P1", p1, 0));
        order.addProduct(new DigitalProd("P2", p2));

        float total = order.calcTotal();

        assertEquals(expected, total, "El total calculado no coincide con el esperado");
    }

    @Test
    @DisplayName("CP-07: Lanzar IllegalStateException si se calcula total de pedido vacío")
    void testEmptyOrderThrowsException() {
        Client client = new Client("Homer", "homer@test.com", "Evergreen");
        
        Order emptyOrder = new Order(client); 

        assertThrows(IllegalStateException.class, emptyOrder::calcTotal, "Debería lanzar IllegalStateException detallando que el pedido está vacío");
    }

    @ParameterizedTest
    @DisplayName("CP-10: Validar que el total NO coincida con valores erróneos")
    @ValueSource(floats = {50.0f, 60.0f, 0.0f, -59.5f})
    void testCalcTotalIncorrectValues(float incorrectValue) {
        Order order = new Order(new Client("Test", "test@test.es", "Calle"));
        order.addProduct(new PhysicalProd("P1", 59.5f, 0)); // Total real 59.5
        
        float total = order.calcTotal();

        assertNotEquals(incorrectValue, total, "El total debería ser distinto a este valor incorrecto");
    }

    @Test
    @DisplayName("Verificar que showSummary devuelve una cadena no nula")
    void testShowSummary() {
        Client client = new Client("Homer", "homer@test.es", "Evergreen");
        Order order = new Order(client);
        order.addProduct(new PhysicalProd("Duff", 10.0f, 2.0f));

        String summary = order.showSummary();

        assertNotNull(summary, "El resumen no debería ser nulo");
        assertTrue(summary.contains("Homer"), "El resumen debe contener el nombre del cliente");
        assertTrue(summary.contains("Duff"), "El resumen debe contener el nombre del producto");
    }
}
