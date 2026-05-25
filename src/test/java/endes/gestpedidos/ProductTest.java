package endes.gestpedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    @DisplayName("CP-01: DigitalProd debe aplicar un 5% de descuento")
    void testDigitalProdCalcPrice() {
        DigitalProd digitalProduct = new DigitalProd(1,"Video Topo", 10.0f, "Pro", 500.0f);

        float finalPrice = digitalProduct.calcPrice();

        assertEquals(9.5f, finalPrice, "El precio con descuento debería ser 9.5f");
    }

    @Test
    @DisplayName("CP-02: PhysicalProd debe sumar el coste de envío al precio base")
    void testPhysicalProdCalcPrice() {
        PhysicalProd physicalProduct = new PhysicalProd(1,"Detergente", 20.0f, 10.0f);

        float finalPrice = physicalProduct.calcPrice();

        assertEquals(30.0f, finalPrice, "El precio total debería ser 30.0f (20.0f + 10.0f)");
    }

    @Test
    @DisplayName("CP-06: Lanzar IllegalArgumentException si el precio es negativo")
    void testNegativePriceThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            
            new PhysicalProd(1,"Error Product", -5.0f, 10.0f);
            
        }, "Debería lanzar IllegalArgumentException al intentar poner un precio negativo");
    }

    @Test
    @DisplayName("CP-08: El precio de un producto digital NO equivale a su precio base")
    void testDigitalPriceNotEqualsBase() {
        DigitalProd digitalProduct = new DigitalProd(1,"Video", 50.0f, "Lic", 10.0f);
        float finalPrice = digitalProduct.calcPrice();
        assertNotEquals(50.0f, finalPrice, "El precio final con descuento no debe ser igual al precio bruto");
    }

    @Test
    @DisplayName("CP-09: El precio de un producto físico NO equivale a su precio base")
    void testPhysicalPriceNotEqualsBase() {
        PhysicalProd physicalProduct = new PhysicalProd(1,"Detergente", 100.0f, 5.0f);
        float finalPrice = physicalProduct.calcPrice();
        assertNotEquals(100.0f, finalPrice, "El precio final debe incluir el envío, no ser igual al base");
    }

    @Test
    @DisplayName("Test de cobertura para setters, getters y toString")
    void testProductCoverage() {
        PhysicalProd p = new PhysicalProd(1,"Duff", 10.0f, 2.0f);
        p.setName("Duff Premium");
        p.setPrice(12.0f);
        
        assertEquals("Duff Premium", p.getName());
        assertEquals(12.0f, p.getPrice());
        assertNotNull(p.toString());
        
        DigitalProd d = new DigitalProd(2,"Video", 5.0f, "Lic", 100.0f);
        d.setLicense("Premium");
        d.setSizeInMb(200.0f);
        
        assertEquals("Premium", d.getLicense());
        assertEquals(200.0f, d.getSizeInMb());
        assertNotNull(d.toString());
    }
}
