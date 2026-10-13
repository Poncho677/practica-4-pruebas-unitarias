import pytest


from estacionamiento import Estacionamiento


@pytest.fixture
def sistema():
    """Preparación reutilizable para las pruebas."""
    return Estacionamiento()


#  Casos normales.

# R5: 1 a 15 minutos -> $0.00
def test_normal_10_minutos_cliente_normal_no_paga(sistema):
    
    # Arrange
    minutos = 10
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 0

    
# R6: 16 a 60 minutos -> $20.00
def test_normal_45_minutos_cliente_normal_paga_primera_hora(sistema):
    
    # Arrange
    minutos = 45
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 20

    
# R7: más de 60 minutos -> $20 + $15 por cada hora adicional iniciada
def test_normal_90_minutos_cliente_normal_paga_primera_hora_más_una_hora_adicional(sistema):
    
    # Arrange
    minutos = 90
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 35.0 # Falla: la implementación original devuelve 20.0

    
#  Casos frontera.

# R4: 0 minutos -> $0.00
def test_frontera_0_minutos_gratis(sistema):
    
    # Arrange
    minutos = 0
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 0


# R5: límites del rango gratis (1, 14, 15) -> $0.00
@pytest.mark.parametrize("minutos", [1, 14, 15])
def test_frontera_rango_gratis_limites_inferiores(sistema, minutos):
    
    # Arrange: @pytest.mark.parametrize
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 0.0


# R6: primeros minutos del rango con cobro (16, 17) -> $20.00
@pytest.mark.parametrize("minutos", [16, 17])
def test_frontera_rango_pago_limites_superiores(sistema, minutos):
    
    # Arrange: @pytest.mark.parametrize
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 20.0


# R6: 60 minutos es el último del rango "16 a 60" -> $20.00
def test_frontera_60_minutos_paga_primera_hora(sistema):
    
    # Arrange
    minutos = 60
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 20.0


# R7: 120 minutos = 60 + 60 -> 1 hora adicional exacta -> $35.00
def test_frontera_120_minutos_una_hora_adicional_exacta(sistema):
    
    # Arrange
    minutos = 120
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 35.0


# R7: 121 minutos -> 2 horas adicionales iniciadas -> $50.00
def test_frontera_121_minutos_dos_horas_adicionales(sistema):
    
    # Arrange
    minutos = 121
    
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    
    # Assert
    assert resultado == 50.0 # Falla: la implementación original devuelve 35.0

    
#Entradas inválidas.

# R1 y R10: minutos negativos -> ValueError
def test_invalido_minutos_negativos_lanza_error(sistema):
    
    # Arrange
    minutos = -1
    
    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, "normal", False)


# R2 y R11: "Normal" no es válido (distingue mayúsculas) -> ValueError
def test_invalido_tipo_cliente_con_mayuscula_lanza_error(sistema):
    
    # Arrange
    minutos = 30
    
    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, "Normal", False)

# Interacciones entre reglas.

# R7 y R8: más de 60 min con cliente frecuente -> $35.00 - 10% = $31.50
def test_interaccion_61_minutos_frecuente_aplica_descuento(sistema):
    
    # Arrange
    minutos = 61
    
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    
    # Assert
    assert resultado == pytest.approx(31.50) # Falla: la implementación original devuelve 18.0


# R5 y R8: frontera 15 min + descuento -> $0.00 - 10% = $0.00
def test_interaccion_15_minutos_frecuente_sigue_siendo_gratis(sistema):
    
    # Arrange
    minutos = 15
    
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    
    # Assert
    assert resultado == pytest.approx(0.0) # Falla: la implementación original devuelve 18.0

# R3 y R8: boleto perdido + cliente frecuente -> $300.00 (el descuento no aplica)
def test_interaccion_10_minutos_frecuente_boleto_perdido_no_recibe_descuento(sistema):
    
    # Arrange
    minutos = 10
    
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", True)
    
    # Assert
    assert resultado == pytest.approx(300.0) # Falla: la implementación original devuelve 270.0


# Números decimales

# R7 y R8: 181 min frecuente -> $65.00 - 10% = $58.50 (resultado con decimales)
def test_decimales_181_minutos_frecuente(sistema):
    
    # Arrange
    minutos = 181
    
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    
    # Assert
    assert resultado == pytest.approx(58.50)  # Falla: la implementación original devuelve 45.0


# Caso adicional

# R9: el costo nunca puede ser negativo (0 a 600 min, ambos tipos, con/sin boleto perdido)
@pytest.mark.parametrize("minutos", [0, 15, 16, 60, 61, 120, 181, 600])
@pytest.mark.parametrize("tipo", ["normal", "frecuente"])
@pytest.mark.parametrize("perdido", [False, True])
def test_adicional_costo_nunca_negativo(sistema, minutos, tipo, perdido):
    # Arrange / Act
    resultado = sistema.calcular_total(minutos, tipo, perdido)
    # Assert
    assert resultado >= 0.0
