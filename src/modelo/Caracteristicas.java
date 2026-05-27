package modelo;

public class Caracteristicas {

    private double kilometraje;
    private double pesoCargaUtil;
    private int horasRalenti;
    private double temperaturaPromedio;
    private String tipoTopografia;

    public Caracteristicas() {
    }

    public Caracteristicas(double kilometraje, double pesoCargaUtil, int horasRalenti, double temperaturaPromedio, String tipoTopografia) {
        this.kilometraje = kilometraje;
        this.pesoCargaUtil = pesoCargaUtil;
        this.horasRalenti = horasRalenti;
        this.temperaturaPromedio = temperaturaPromedio;
        this.tipoTopografia = tipoTopografia;
    }

    public double calcularMultiplicadorDesgaste() {
        double multiplicadorBase = 1.0;

        if (this.tipoTopografia != null && this.tipoTopografia.equalsIgnoreCase("Montañosa")) {
            multiplicadorBase += 0.30;
        }

        if (this.horasRalenti > 50) {
            multiplicadorBase += 0.15;
        }

        if (this.pesoCargaUtil > 2000) {
            multiplicadorBase += 0.20;
        }

        return multiplicadorBase;
    }

    public double getKilometraje() { return kilometraje; }
    public void setKilometraje(double kilometraje) { this.kilometraje = kilometraje; }

    public double getPesoCargaUtil() { return pesoCargaUtil; }
    public void setPesoCargaUtil(double pesoCargaUtil) { this.pesoCargaUtil = pesoCargaUtil; }

    public int getHorasRalenti() { return horasRalenti; }
    public void setHorasRalenti(int horasRalenti) { this.horasRalenti = horasRalenti; }

    public double getTemperaturaPromedio() { return temperaturaPromedio; }
    public void setTemperaturaPromedio(double temperaturaPromedio) { this.temperaturaPromedio = temperaturaPromedio; }

    public String getTipoTopografia() { return tipoTopografia; }
    public void setTipoTopografia(String tipoTopografia) { this.tipoTopografia = tipoTopografia; }

    public void mostrarCaracteristicas() {
        System.out.println("=== Características de Operación ===");
        System.out.println("Kilometraje:          " + this.kilometraje + " km");
        System.out.println("Peso Carga Útil:      " + this.pesoCargaUtil + " Kg");
        System.out.println("Horas en Ralentí:     " + this.horasRalenti + " h");
        System.out.println("Temperatura Promedio: " + this.temperaturaPromedio + " °C");
        System.out.println("Tipo de Topografía:   " + this.tipoTopografia);
        System.out.println("Multiplicador Actual: " + this.calcularMultiplicadorDesgaste() + "x");
        System.out.println("====================================");
    }

    @Override
    public String toString() {
        return "Caracteristicas {" +
                "kilometraje=" + kilometraje +
                ", pesoCargaUtil=" + pesoCargaUtil +
                ", horasRalenti=" + horasRalenti +
                ", temperaturaPromedio=" + temperaturaPromedio +
                ", tipoTopografia='" + tipoTopografia + '\'' +
                '}';
    }
}