package negocio;

import java.util.ArrayList;
import java.util.List;

public class HistorialMantenimiento {
    private List<String> registros;

    public HistorialMantenimiento() {
        this.registros = new ArrayList<>();
    }

    // Almacena un reporte o alerta generada en el historial corporativo
    public void agregarRegistro(String reporte) {
        if (reporte != null && !reporte.isEmpty()) {
            registros.add(reporte);
        }
    }

    // Devuelve todo el historial acumulado en un bloque de texto ordenado
    public String obtenerHistorialCompleto() {
        if (registros.isEmpty()) {
            return "No hay análisis guardados en el historial.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========== HISTORIAL DE REVISIONES ACUMULADAS ==========\n\n");
        for (int i = 0; i < registros.size(); i++) {
            sb.append("ANÁLISIS #").append(i + 1).append(":\n")
                    .append(registros.get(i))
                    .append("\n--------------------------------------------------------\n");
        }
        return sb.toString();
    }

    public List<String> getRegistros() {
        return registros;
    }
}