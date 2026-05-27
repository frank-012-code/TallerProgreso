package interfaz;

import modelo.Caracteristicas;               // Importación requerida
import negocio.EstimadorMantenimiento;       // Importación requerida
import negocio.GestorAlertas;               // Importación requerida

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JTextField txtKm, txtCarga, txtRalenti, txtTemp;
    private JComboBox<String> cbTopografia;
    private JTextArea areaResultado;

    public Main() {
        setTitle("Sistema de Predicción de Mantenimiento Vehicular");
        setSize(600, 500);
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

            StringBuilder sb = new StringBuilder();
            sb.append("--- RESULTADOS DEL ANÁLISIS ---\n");
            sb.append("Multiplicador de Desgaste: ").append(op.calcularMultiplicadorDesgaste()).append("x\n\n");
            sb.append(estimador.obtenerEstimacionCambios(op)).append("\n");

            areaResultado.setText(sb.toString());

            gestor.evaluarEstadoComponente("Vehículo Usuario", "Aceite Motor", 5000.0, op);

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