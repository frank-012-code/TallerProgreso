package negocio;

import modelo.Caracteristicas; // Importación necesaria
import java.util.ArrayList;
import java.util.List;

public class GestorAlertas {

    private List<String> historialAlertas;

    public GestorAlertas() {
        this.historialAlertas = new ArrayList<>();
    }

    public void evaluarEstadoComponente(String nombreVehiculo, String nombreComponente, double vidaUtilBaseKm, Caracteristicas operacion) {

        double multiplicador = operacion.calcularMultiplicadorDesgaste();
        double vidaUtilRealKm = vidaUtilBaseKm / multiplicador;
        double kmActuales = operacion.getKilometraje();
        double porcentajeDesgaste = (kmActuales / vidaUtilRealKm) * 100;

        System.out.println("--- DIAGNÓSTICO DEL SISTEMA ---");
        System.out.println("Vehículo: " + nombreVehiculo + " | Componente: " + nombreComponente);
        System.out.println("Desgaste Actual: " + String.format("%.2f", porcentajeDesgaste) + "%");

        if (porcentajeDesgaste >= 100) {
            registrarAlerta("CRÍTICO: El componente '" + nombreComponente + "' del " + nombreVehiculo + " ha superado su vida útil límite (" + kmActuales + " km). Reemplazo inmediato.");
        } else if (porcentajeDesgaste >= 85) {
            registrarAlerta("ADVERTENCIA PREVENTIVA: El componente '" + nombreComponente + "' del " + nombreVehiculo + " está al " + String.format("%.2f", porcentajeDesgaste) + "% de desgaste. Proyectar repuesto en stock.");
        } else {
            System.out.println("Estado: ÓPTIMO. No se requieren acciones.");
        }
        System.out.println("-------------------------------\n");
    }

    private void registrarAlerta(String mensaje) {
        historialAlertas.add(mensaje);
        System.out.println(">> ALERTA GENERADA: " + mensaje);
    }

    public void imprimirHistorialAlertas() {
        System.out.println("=== HISTORIAL DE ALERTAS Y REPUESTOS ===");
        if (historialAlertas.isEmpty()) {
            System.out.println("No hay alertas pendientes.");
        } else {
            for (int i = 0; i < historialAlertas.size(); i++) {
                System.out.println((i + 1) + ". " + historialAlertas.get(i));
            }
        }
        System.out.println("========================================");
    }
}