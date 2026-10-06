
import pytest

from estacionamiento import Estacionamiento


@pytest.fixture
def sistema():
    """Preparación reutilizable para las pruebas."""
    return Estacionamiento()


def test_ejemplo_inicial(sistema):
    """
    Esta prueba sólo sirve como punto de partida.
    El alumno debe reemplazarla/complementarla con su diseño de casos.
    """
    # Arrange
    minutos = 10

    # Act
    resultado = sistema.calcular_total(minutos, "normal", False)

    # Assert
    assert resultado == 0.0


# TODO:
# 1. Agregue casos normales.
# 2. Agregue casos frontera.
# 3. Agregue entradas inválidas con pytest.raises.
# 4. Agregue casos parametrizados con @pytest.mark.parametrize.
# 5. Use pytest.approx cuando el resultado esperado tenga decimales.
# 6. Pruebe interacciones entre reglas.
