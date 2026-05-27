package interfaz;

import modelo.Caracteristicas;               // Importación requerida
import negocio.EstimadorMantenimiento;       // Importación requerida
import negocio.GestorAlertas;               // Importación requerida
import negocio.HistorialMantenimiento;       // Importación requerida para la 5ta clase

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JTextField txtKm, txtCarga, txtRalenti, txtTemp;
    private JComboBox<String> cbTopografia;
    private JTextArea areaResultado;

    // Instancia global del Historial de Mantenimiento
    private HistorialMantenimiento historial = new HistorialMantenimiento();

    public Main() {
        setTitle("Sistema de Predicción de Mantenimiento Vehicular");
        setSize(600, 550); // Ajustamos ligeramente el alto para visualizar el historial cómodamente
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelInput.add(new JLabel("Kilometraje Actual (km):"));
        txtKm = new JTextField();
        panelInput.add(txtKm);

        panelInput.add(new JLabel("Carga Útil (kg):"));
        txtCarga = new JTextField();
        panelInput.add(txtCarga);

        panelInput.add(new JLabel("Horas Ralentí:"));
        txtRalenti = new JTextField();
        panelInput.add(txtRalenti);

        panelInput.add(new JLabel("Temperatura Promedio (°C):"));
        txtTemp = new JTextField();
        panelInput.add(txtTemp);

        panelInput.add(new JLabel("Topografía:"));
        String[] opciones = {"Plana", "Ondulada", "Montañosa"};
        cbTopografia = new JComboBox<>(opciones);
        panelInput.add(cbTopografia);

        JButton btnCalcular = new JButton("Calcular Predicción");
        btnCalcular.setBackground(new Color(46, 204, 113));
        btnCalcular.setForeground(Color.WHITE);
        panelInput.add(btnCalcular);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelInput, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarLogica();
            }
        });
    }

    private void ejecutarLogica() {
        try {
            double km = Double.parseDouble(txtKm.getText());
            double carga = Double.parseDouble(txtCarga.getText());
            int ralenti = Integer.parseInt(txtRalenti.getText());
            double temp = Double.parseDouble(txtTemp.getText());
            String topo = (String) cbTopografia.getSelectedItem();

            Caracteristicas op = new Caracteristicas(km, carga, ralenti, temp, topo);

            EstimadorMantenimiento estimador = new EstimadorMantenimiento();
            GestorAlertas gestor = new GestorAlertas();

            // 1. Ejecutar las alertas en la consola y capturar si hay alguna crítica/preventiva
            String alertaFrenos = gestor.evaluarEstadoComponente("Vehículo Usuario", "Pastillas Frenos", 20000.0, op);
            String alertaAceite = gestor.evaluarEstadoComponente("Vehículo Usuario", "Aceite Motor", 5000.0, op);
            String alertaFiltros = gestor.evaluarEstadoComponente("Vehículo Usuario", "Filtros de Aire", 10000.0, op);

            // 2. Construir el reporte del cálculo actual
            StringBuilder reporteActual = new StringBuilder();
            reporteActual.append("Multiplicador de Desgaste: ").append(op.calcularMultiplicadorDesgaste()).append("x\n");
            reporteActual.append(estimador.obtenerEstimacionCambios(op));

            // Si hubo alertas críticas en el diagnóstico del Gestor, las añadimos al reporte
            if (!alertaAceite.isEmpty() || !alertaFrenos.isEmpty() || !alertaFiltros.isEmpty()) {
                reporteActual.append("\n⚠️ ALERTAS DETECTADAS EN REVISIÓN:\n");
                if (!alertaAceite.isEmpty()) reporteActual.append(" - ").append(alertaAceite).append("\n");
                if (!alertaFrenos.isEmpty()) reporteActual.append(" - ").append(alertaFrenos).append("\n");
                if (!alertaFiltros.isEmpty()) reporteActual.append(" - ").append(alertaFiltros).append("\n");
            }

            // 3. Registrar el reporte consolidado en la nueva clase Historial
            historial.agregarRegistro(reporteActual.toString());

            // 4. Mostrar en la caja de texto el historial completo con todas las consultas acumuladas
            areaResultado.setText(historial.obtenerHistorialCompleto());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa valores numéricos válidos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}