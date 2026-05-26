package dev.jfgonzalez.gestpedidos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import dev.jfgonzalez.gestpedidos.model.Customer;
import dev.jfgonzalez.gestpedidos.model.DigitalProduct;
import dev.jfgonzalez.gestpedidos.model.Order;
import dev.jfgonzalez.gestpedidos.model.PhysicalProduct;

class OrderTest {
    @ParameterizedTest
    @DisplayName("CP-03: Cálculo correcto del total con varios productos")
    @CsvSource({
        "14.75, 10.0, 5.0",   // 10 + 5 = 15
        "24.75, 20.0, 5.0",   // 20 + 5 = 25
        "48.75, 25.0, 25.0"   // 25 + 25 = 50
    })
    void testCalcTotalParameterized(float expected, float price1, float price2) {
        Order order = new Order(1, new Customer(1, "Test"));
        order.addProduct(new PhysicalProduct(1,"P1", price1));
        order.addProduct(new DigitalProduct(2,"P2", price2));

        float total = order.calcTotal();

        assertEquals(expected, total, "El total calculado no coincide con el esperado");
    }

    @Test
    @DisplayName("CP-07: Lanzar IllegalStateException si se calcula total de pedido vacío")
    void testEmptyOrderThrowsException() {
        Customer client = new Customer(1, "Homer");
        
        Order emptyOrder = new Order(1,client); 

        assertThrows(IllegalStateException.class, emptyOrder::calcTotal, "Debería lanzar IllegalStateException detallando que el pedido está vacío");
    }

    @ParameterizedTest
    @DisplayName("CP-10: Validar que el total NO coincida con valores erróneos")
    @ValueSource(floats = {50.0f, 60.0f, 0.0f, -59.5f})
    void testCalcTotalIncorrectValues(float incorrectValue) {
        Order order = new Order(1, new Customer(1,"Test"));
        order.addProduct(new PhysicalProduct(1,"P1", 59.5f)); // Total real 59.5
        
        float total = order.calcTotal();

        assertNotEquals(incorrectValue, total, "El total debería ser distinto a este valor incorrecto");
    }

    @Test
    @DisplayName("Verificar que showSummary devuelve una cadena no nula")
    void testShowSummary() {
        Customer client = new Customer(1, "Homer");
        Order order = new Order(1, client);
        order.addProduct(new PhysicalProduct(1,"Duff", 10.0f));

        String summary = order.showSummary();

        assertNotNull(summary, "El resumen no debería ser nulo");
        assertTrue(summary.contains("Homer"), "El resumen debe contener el nombre del cliente");
        assertTrue(summary.contains("Duff"), "El resumen debe contener el nombre del producto");
    }
}
