package dev.jfgonzalez.gestpedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductoTest {
    @Test
    @DisplayName("CP-01: ProductoDigital debe aplicar el IVA correctamente")
    void testDigitalProdCalcPrice() {
        ProductoDigital pd = new ProductoDigital(1,"test_product", 10);
        pd.aplicarIva("REDUCIDO");
        
        double precioConIva = pd.calcularPrecioFinal();

        assertEquals(11, precioConIva, "El precio con IVA debería ser 11");
    }

    @Test
    @DisplayName("CP-06: Lanzar IllegalArgumentException si el precio es negativo")
    void testNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            
            new ProductoFisico(1,"Error Product", -5.0f);
            
        }, "Debería lanzar IllegalArgumentException al intentar poner un precio negativo");
    }

    @Test
    @DisplayName("CP-08: El precio de un producto digital NO equivale a su precio base")
    void testDigitalPriceNotEqualsBase() {
        ProductoDigital digitalProduct = new ProductoDigital(1,"Video", 50.0f);

        double finalPrice = digitalProduct.calcularPrecioFinal();

        assertNotEquals(50.0f, finalPrice, "El precio final con descuento no debe ser igual al precio bruto");
    }

    @Test
    @DisplayName("CP-09: El precio de un pedido con productos físicos NO equivale la suma de sus precios base")
    void testPhysicalPriceNotEqualsBase() {
        ProductoFisico pf = new ProductoFisico(1,"Detergente", 50, 10);
        Map<Integer,Integer> cantidades = new HashMap<>();
        cantidades.put(pf.getId(), 2);
        Cliente cliente = new Cliente();
        Pedido pedido = new Pedido(0, cliente, List.of(pf),cantidades);

        double precioFinal = pedido.calcularTotal() + pedido.calcularEnvio(null);

        assertNotEquals(pf.getPrecioBase() * 2, precioFinal, "El precio final debe incluir el envío, no ser igual al base");
    }
}
