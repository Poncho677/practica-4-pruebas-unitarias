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
    assert resultado == 0.0


def test_normal_45_minutos_
#  Casos frontera.


#Entradas inválidas.
"""
# Usar pytest.raises.
# Casos parametrizados con @pytest.mark.parametrize.
# Use pytest.approx cuando el resultado esperado tenga decimales.
"""

# Interacciones entre reglas.


