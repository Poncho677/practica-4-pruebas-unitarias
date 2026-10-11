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
    assert resultado == 35
#  Casos frontera.


#Entradas inválidas.
"""
# Usar pytest.raises.
# Casos parametrizados con @pytest.mark.parametrize.
# Use pytest.approx cuando el resultado esperado tenga decimales.
"""

# Interacciones entre reglas.


