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
 
    // R6: 16 a 60 minutos -> $20.00
    @Test
    void testNormal45MinutosClienteNormalPagaPrimeraHora() {
        // Arrange
        int minutos = 45;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(20.0, resultado, 0.001);
    }
 
    // R7: más de 60 minutos -> $20 + $15 por cada hora adicional iniciada
    @Test
    void testNormal90MinutosClienteNormalPagaPrimeraHoraMasUnaHoraAdicional() {
        // Arrange
        int minutos = 90;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(35.0, resultado, 0.001); // Falla: la implementación original devuelve 20.0
    }
    
    // Casos frontera

    // R4: 0 minutos -> $0.00
    @Test
    void testFrontera0MinutosGratis() {
        // Arrange
        int minutos = 0;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(0.0, resultado, 0.001);
    }
 
    // R5: límites del rango gratis (1, 14, 15) -> $0.00
    @ParameterizedTest
    @ValueSource(ints = {1, 14, 15})
    void testFronteraRangoGratisLimitesInferiores(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(0.0, resultado, 0.001); // Falla con 15: la implementación original devuelve 20.0
    }
 
    // R6: primeros minutos del rango con cobro (16, 17) -> $20.00
    @ParameterizedTest
    @ValueSource(ints = {16, 17})
    void testFronteraRangoPagoLimitesSuperiores(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(20.0, resultado, 0.001);
    }
 
    // R6: 60 minutos es el último del rango "16 a 60" -> $20.00
    @Test
    void testFrontera60MinutosPagaPrimeraHora() {
        // Arrange
        int minutos = 60;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(20.0, resultado, 0.001);
    }
 
    // R7: 120 minutos = 60 + 60 -> 1 hora adicional exacta -> $35.00
    @Test
    void testFrontera120MinutosUnaHoraAdicionalExacta() {
        // Arrange
        int minutos = 120;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(35.0, resultado, 0.001);
    }
 
    // R7: 121 minutos -> 2 horas adicionales iniciadas -> $50.00
    @Test
    void testFrontera121MinutosDosHorasAdicionales() {
        // Arrange
        int minutos = 121;
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(50.0, resultado, 0.001); // Falla: la implementación original devuelve 35.0
    }
    
    // Entradas inválidas

    // R1 y R10: minutos negativos -> IllegalArgumentException
    @Test
    void testInvalidoMinutosNegativosLanzaError() {
        // Arrange
        int minutos = -1;
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.calcularTotal(minutos, "normal", false);
        });
    }
 
    // R2 y R11: "Normal" no es válido (distingue mayúsculas) -> IllegalArgumentException
    @Test
    void testInvalidoTipoClienteConMayusculaLanzaError() {
        // Arrange
        int minutos = 30;
        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.calcularTotal(minutos, "Normal", false);
        });
    }
    
    // Interacciones entre reglas

    // R7 y R8: más de 60 min con cliente frecuente -> $35.00 - 10% = $31.50
    @Test
    void testInteraccion61MinutosFrecuenteAplicaDescuento() {
        // Arrange
        int minutos = 61;
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);
        // Assert
        assertEquals(31.50, resultado, 0.001); // Falla: la implementación original devuelve 18.0
    }
 
    // R5 y R8: frontera 15 min + descuento -> $0.00 - 10% = $0.00
    @Test
    void testInteraccion15MinutosFrecuenteSigueSiendoGratis() {
        // Arrange
        int minutos = 15;
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);
        // Assert
        assertEquals(0.0, resultado, 0.001); // Falla: la implementación original devuelve 18.0
    }
 
    // R3 y R8: boleto perdido + cliente frecuente -> $300.00 (el descuento no aplica)
    @Test
    void testInteraccion10MinutosFrecuenteBoletoPerdidoNoRecibeDescuento() {
        // Arrange
        int minutos = 10;
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", true);
        // Assert
        assertEquals(300.0, resultado, 0.001); // Falla: la implementación original devuelve 270.0
    }
    
    // Números decimales

    // R7 y R8: 181 min frecuente -> $65.00 - 10% = $58.50 (resultado con decimales)
    @Test
    void testDecimales181MinutosFrecuente() {
        // Arrange
        int minutos = 181;
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);
        // Assert
        assertEquals(58.50, resultado, 0.001); // Falla: la implementación original devuelve 45.0
    }
    
    // Caso adicional

    // R9: el costo nunca puede ser negativo (0 a 600 min, ambos tipos, con/sin boleto perdido).
    // Se prueba cada combinación de tipo y boleto con una prueba parametrizada por minutos.
    @ParameterizedTest
    @ValueSource(ints = {0, 15, 16, 60, 61, 120, 181, 600})
    void testAdicionalCostoNuncaNegativoNormalSinBoletoPerdido(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", false);
        // Assert
        assertEquals(true, resultado >= 0.0, "El costo no debe ser negativo");
    }
 
    @ParameterizedTest
    @ValueSource(ints = {0, 15, 16, 60, 61, 120, 181, 600})
    void testAdicionalCostoNuncaNegativoFrecuenteSinBoletoPerdido(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", false);
        // Assert
        assertEquals(true, resultado >= 0.0, "El costo no debe ser negativo");
    }
 
    @ParameterizedTest
    @ValueSource(ints = {0, 15, 16, 60, 61, 120, 181, 600})
    void testAdicionalCostoNuncaNegativoNormalConBoletoPerdido(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "normal", true);
        // Assert
        assertEquals(true, resultado >= 0.0, "El costo no debe ser negativo");
    }
 
    @ParameterizedTest
    @ValueSource(ints = {0, 15, 16, 60, 61, 120, 181, 600})
    void testAdicionalCostoNuncaNegativoFrecuenteConBoletoPerdido(int minutos) {
        // Arrange: @ParameterizedTest
        // Act
        double resultado = sistema.calcularTotal(minutos, "frecuente", true);
        // Assert
        assertEquals(true, resultado >= 0.0, "El costo no debe ser negativo");
    }
}
