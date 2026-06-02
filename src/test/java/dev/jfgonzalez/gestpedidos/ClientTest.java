package dev.jfgonzalez.gestpedidos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dev.jfgonzalez.gestpedidos.model.Cliente;

class ClientTest {
    @Test
    @DisplayName("Test de cobertura para setters, getters y toString")
    void testClientCoverage() {
        Cliente c = new Cliente(1,"name");
        c.setNombre("new name");
        c.setEmail("mail@test.dev");
        c.setDireccion("address");
        
        assertEquals("new name", c.getNombre());
        assertEquals("mail@test.dev", c.getEmail());
        assertEquals("address", c.getDireccion());
        assertNotNull(c.toString());
    }
}
