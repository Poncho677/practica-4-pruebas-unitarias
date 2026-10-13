package mx.edu.practica4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstacionamientoTest {

    private Estacionamiento sistema;

    @BeforeEach
    void setUp() {
        sistema = new Estacionamiento();
    }

    // Casos normales

    // R5: 1 a 15 minutos -> $0.00
    @Test
    void testNormal10MinutosClienteNormalNoPaga() {
        // Arrange
        int minutos = 10;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(0.0, resultado, 0.001);
    }
}
