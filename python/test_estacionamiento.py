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
    assert resultado == 35 # Falla: la implementación devuelve 20

    
#  Casos frontera.

def test_frontera_0_minutos_gratis(sistema):
    # Arrange
    minutos = 0
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 0


@pytest.mark.parametrize("minutos", [1, 14, 15])
def test_frontera_limites_del_rango_gratis(sistema, minutos):
    # Arrange: @pytest.mark.parametrize
    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)
    # Assert
    assert resultado == 0.0


@pytest.mark.parametrize("minutos", [16, 17])
def test_frontera_limites_del_rango_gratis(sistema, minutos):
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
    assert resultado == 50.0 # Falla: la implementación da 35

    
#Entradas inválidas.
"""
# Usar pytest.raises.
# Casos parametrizados con @pytest.mark.parametrize.
# Use pytest.approx cuando el resultado esperado tenga decimales.
"""

# Interacciones entre reglas.


