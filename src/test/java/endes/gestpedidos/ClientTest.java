package endes.gestpedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClientTest {
    @Test
    @DisplayName("Test de cobertura para setters, getters y toString")
    void testClientCoverage() {
        Client c = new Client("Homer", "homer@test.es", "Evergreen");
        c.setName("Marge");
        c.setMail("marge@test.es");
        c.setAddress("Calle 123");
        
        assertEquals("Marge", c.getName());
        assertEquals("marge@test.es", c.getMail());
        assertEquals("Calle 123", c.getAddress());
        assertNotNull(c.toString());
    }
}
