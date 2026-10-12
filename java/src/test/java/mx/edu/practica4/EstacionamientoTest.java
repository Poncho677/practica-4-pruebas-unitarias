package mx.edu.practica4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.Transient;

class EstacionamientoTest {

    private Estacionamiento sistema;

    @BeforeEach
    void setUp() {
        sistema = new Estacionamiento();
    }

    // Casos normales.

    @Test
    void testCasoNormal1_10MinutosNormalNoPaga() {
        // Arrange
        int minutos = 10;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    void testCasoNormal2_45MinutosNormalPagaPrimeraHora() {
        // Arrange
        int minutos = 45;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(20.0, resultado, 0.001);
    }

    @Test
    void testCasoNormal3_90MinutosNormalPagaPrimeraHoraMasUnaAdicional() {
        // Arrange
        int minutos = 90;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(35.0, resultado, 0.001);
    }

    // Casos frontera.

    @Test
    void testCasoFrontera1_0MinutosGratis() {
        // Arrange
        int minutos = 0;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 14, 15})
    void testCasosFrontera2a4_RangoGratisLimites(int minutos) {
        // Arrange & Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 17})
    void testCasosFrontera5y6_RangoPagoPrimerosMinutos(int minutos){
        // Arrange y Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(20.0, resultado, 0.001);
    }

    @Test
    void testCasoFrontera7_60MinutosPagaPrimeraHora() {
        // Arrange
        int minutos = 60;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(20.0, resultado, 0.001);
    }

    @Test
    void testCasoFrontera8_120MinutosUnaHoraAdicionalExacta() {
        // Arrange
        int minutos = 120;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(35.0, resultado, 0.001);
    }

    @Test
    void testCasoFrontera9_121MinutosDosHorasAdicionalesIniciadas() {
        // Arrange
        int minutos = 121;

        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);

        // Assert
        assertEquals(50.0, resultado, 0.001);
    }

    // Entradas inválidas.

    @Test
    void testEntradaInvalida1_MinutosNegativosLanzaExcepcion() {
        // Arrange
        int minutos = -1;

        // Act y Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.calcularTotal(minutos, "normal", false);
        });
    }

    @Test
    void testEntradaInvalida2_TipoClienteNormalConMayusculaLanzaExcepcion() {
        // Arrange
        int minutos = 30;

        // Act y Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.calcularTotal(minutos, "Normal", false);
        });
    }

    // Interacciones entre reglas.

    @Test
    void testInteraccion1_61MinutosFrecuenteAplicaDescuento() {
        // Arrange
        int minutos = 61;

        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);

        // Assert
        assertEquals(31.50, resultado, 0.001);
    }

    @Test
    void testInteraccion2_15MinutosFrecuenteSigueSiendoGratis() {
        // Arrange
        int minutos = 15;

        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);

        // Assert
        assertEquals(0.0, resultado, 0.001);
    }

    // Números decimales.
    @Test
    void testNumerosDecimales1_181MinutosFrecuente() {
        // Arrange
        int minutos = 181;

        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);

        // Assert
        assertEquals(58.50, resultado, 0.001);
    }

    @Test
    void testBoletoPerdido_frecuente() {
	// Arrange
	int[] minutos = {0, 10, 15, 60, 78, 120, 121, 200};

	//Act
	for(int i = 0; i < minutos.length; i++){
	    double resultado = sistema.calcularTotal(minutos[i], "frecuente", true);
	    // Assert
	    assertEquals(300, resultado);
	}
    }

    // Caso adicional
    @ParameterizedTest
    @CsvSource({"0, normal, false", "1, normal, false", "14, normal, false", "15, normal, false", "16, normal, false", "17, normal, false", "60, normal, false", "61, frecuente, false", "120, normal, false", "121, normal, false", "181, frecuente, false", "600, normal, true", "600, frecuente, true"})
    void testCasoAdicional1_CostoNuncaEsNegativo(int minutos, String tipoCliente, boolean boletoPerdido) {
        // Arrange y Act
        double resultado = sistema.calcularTotal(minutos, tipoCliente, boletoPerdido);

        // Assert
        assertTrue(resultado >= 0.0, "El costo no debe ser negativo");
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
}
