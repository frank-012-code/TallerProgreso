package negocio;

import modelo.Caracteristicas; // Importación necesaria

public class EstimadorMantenimiento {

    private final double LIMITE_ACEITE = 5000.0;
    private final double LIMITE_FRENOS = 20000.0;
    private final double LIMITE_FILTROS = 10000.0;

    public String obtenerEstimacionCambios(Caracteristicas info) {
        double kilometrajeActual = info.getKilometraje();

        StringBuilder reporte = new StringBuilder();
        reporte.append("===== ESTIMACIÓN DE MANTENIMIENTO =====\n");
        reporte.append("Kilometraje Recorrido: ").append(kilometrajeActual).append(" km\n\n");

        reporte.append(calcularComponente("Aceite Motor", LIMITE_ACEITE, kilometrajeActual));
        reporte.append(calcularComponente("Pastillas Frenos", LIMITE_FRENOS, kilometrajeActual));
        reporte.append(calcularComponente("Filtros de Aire", LIMITE_FILTROS, kilometrajeActual));

        return reporte.toString();
    }

    private String calcularComponente(String nombre, double limite, double actual) {
        double recorridoEnCiclo = actual % limite;
        double faltante = limite - recorridoEnCiclo;
        double porcentaje = (faltante / limite) * 100;

        String alerta = "";
        if (porcentaje <= 15) {
            alerta = " [CAMBIO INMINENTE]";
        } else if (porcentaje <= 30) {
            alerta = " [PROXIMAMENTE]";
        }

        return String.format("%-15s: %5.1f%% vida restante | Cambio en: %6.0f km %s\n",
                nombre, porcentaje, faltante, alerta);
    }
}