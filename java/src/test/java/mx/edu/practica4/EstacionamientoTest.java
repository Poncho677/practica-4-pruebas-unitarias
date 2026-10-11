
package mx.edu.practica4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstacionamientoTest {

    private Estacionamiento sistema;

    @BeforeEach
    void setUp() {
        sistema = new Estacionamiento();
    }

    @Test
    void ejemploInicial() {
        // Arrange
        int minutos = 10;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }

    /*
     * TODO:
     * 1. Agregue casos normales.
     * 2. Agregue casos frontera.
     * 3. Agregue entradas inválidas con assertThrows.
     * 4. Agregue pruebas parametrizadas.
     * 5. Use tolerancia para resultados decimales.
     * 6. Pruebe interacciones entre reglas.
     */
}
