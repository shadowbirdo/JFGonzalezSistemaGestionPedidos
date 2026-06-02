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
        c.setName("new name");
        c.setEmail("mail@test.dev");
        c.setAddress("address");
        
        assertEquals("new name", c.getName());
        assertEquals("mail@test.dev", c.getEmail());
        assertEquals("address", c.getAddress());
        assertNotNull(c.toString());
    }
}
