
import pytest

from estacionamiento import Estacionamiento


@pytest.fixture
def sistema():
    """Preparación reutilizable para las pruebas."""
    return Estacionamiento()


# TODO:
# 1. Casos normales.

def test_normal_10_minutos_cliente_normal_no_paga(sistema):
    # Arrange
    minutos = 10

    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)

    # Assert
    assert resultado == 0.0


# 2. Casos frontera.


# 3. Entradas inválidas.
# Usar pytest.raises.
# Casos parametrizados con @pytest.mark.parametrize.
# Use pytest.approx cuando el resultado esperado tenga decimales.


# 4. Interacciones entre reglas.


