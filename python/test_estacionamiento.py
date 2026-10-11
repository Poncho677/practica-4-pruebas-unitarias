import pytest

from estacionamiento import Estacionamiento


@pytest.fixture
def sistema():
    """Preparación reutilizable para las pruebas."""
    return Estacionamiento()


#  Casos normales.

def test_normal_10_minutos_cliente_normal_no_paga(sistema):
    # Arrange
    minutos = 10
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 0


def test_normal_45_minutos_cliente_normal_paga_primera_hora(sistema):
    # Arrange
    minutos = 45
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 20


def test_normal_90_minutos_cliente_normal_paga_primera_hora_más_una_hora_adicional(sistema):
    # Arrange
    minutos = 90
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 35.0 # Falla: la implementación devuelve 20.0

    
#  Casos frontera.

def test_frontera_0_minutos_gratis(sistema):
    # Arrange
    minutos = 0
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 0


@pytest.mark.parametrize("minutos", [1, 14, 15])
def test_frontera_rango_gratis_limites_inferiores(sistema, minutos):
    # Arrange: @pytest.mark.parametrize
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 0.0


@pytest.mark.parametrize("minutos", [16, 17])
def test_frontera_rango_pago_limites_superiores(sistema, minutos):
    # Arrange: @pytest.mark.parametrize
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 20.0


def test_frontera_60_minutos_paga_primera_hora(sistema):
    # Arrange
    minutos = 60
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 20.0


def test_frontera_120_minutos_una_hora_adicional_exacta(sistema):
    # Arrange
    minutos = 120
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 35.0


def test_frontera_121_minutos_dos_horas_adicionales(sistema):
    # Arrange
    minutos = 121
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 50.0 # Falla: la implementación da 35.0

    
#Entradas inválidas.

def test_invalido_minutos_negativos_lanza_error(sistema):
    # Arrange
    minutos = -1
    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, "normal", False)


def test_invalido_tipo_cliente_con_mayuscula_lanza_error(sistema):
    # Arrange
    minutos = 30
    # Act / Assert
    with pytest.raises(ValueError):
        sistema.calcular_total(minutos, "Normal", False)

# Interacciones entre reglas.

def test_interaccion_61_minutos_frecuente_aplica_descuento(sistema):
    # Arrange
    minutos = 61
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    # Assert
    assert resultado == pytest.approx(31.50) # Falla: la implementación da 18.0


def test_interaccion_15_minutos_frecuente_sigue_siendo_gratis(sistema):
    # Arrange
    minutos = 15
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    # Assert
    assert resultado == pytest.approx(0.0) # Falla: la implementación da 18.0


# Números decimales

def test_decimales_181_minutos_frecuente(sistema):
    # Arrange
    minutos = 181
    # Act
    resultado = sistema.calcular_total(minutos, "frecuente", False)
    # Assert
    assert resultado == pytest.approx(58.50)  # Falla: la implementación devuelve 45.0


# Caso adicional

@pytest.mark.parametrize("minutos", [0, 15, 16, 60, 61, 120, 181, 600])
@pytest.mark.parametrize("tipo", ["normal", "frecuente"])
@pytest.mark.parametrize("perdido", [False, True])
def test_adicional_costo_nunca_negativo(sistema, minutos, tipo, perdido):
    # Arrange / Act
    resultado = sistema.calcular_total(minutos, tipo, perdido)
    # Assert
    assert resultado >= 0.0
