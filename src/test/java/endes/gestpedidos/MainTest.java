package endes.gestpedidos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("Test de cobertura para Main")
    void testMainCoverage() {
        assertDoesNotThrow(Main::executeLogic);
    }
}