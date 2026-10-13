package mx.edu.practica4;

public class Estacionamiento {

    public static final double TARIFA_BOLETO_PERDIDO = 300.0;

    public double calcularTotal(int minutos, String tipoCliente, boolean boletoPerdido) {
        /*
         * Calcula el total a pagar de acuerdo con el modelo de la práctica.
         */
        if (minutos < 0) {
            throw new IllegalArgumentException("Los minutos no pueden ser negativos");
        }

        if (!"normal".equals(tipoCliente) && !"frecuente".equals(tipoCliente)) {
            throw new IllegalArgumentException("Tipo de cliente no válido");
        }

        double total;

        if (boletoPerdido) {
            total = TARIFA_BOLETO_PERDIDO;
        } else if (minutos <= 15) { // Bug 1: antes era "minutos < 15"
            total = 0.0;
        } else if (minutos <= 60) {
            total = 20.0;
        } else {
	    int minutosRestantes = minutos - 60;
            int horasAdicionales = (minutosRestantes + 59) / 60; // Bug 2: redondear hacia arriba
            total = 20.0 + (horasAdicionales * 15.0);
        }

        if ("frecuente".equals(tipoCliente) && !boletoPerdido) { // Bug 3: sin descuento con boleto perdido
            total *= 0.90;
        }

        return Math.round(total * 100.0) / 100.0;
    }
}
