package interfaz;

import modelo.Caracteristicas;
import negocio.EstimadorMantenimiento;
import negocio.GestorAlertas;
import negocio.HistorialMantenimiento;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JFrame {
    private JTextField txtKm, txtCarga, txtRalenti, txtTemp;
    private JComboBox<String> cbTopografia;
    private JTextArea areaResultado;

    private HistorialMantenimiento historial = new HistorialMantenimiento();

    public Main() {
        // Activar el Look & Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el tema del sistema.");
        }

        setTitle("Terminal de Diagnóstico Automotriz - V2.5");
        setSize(750, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PALETA DE COLORES (Estilo Motorizado / Dark Mode) ---
        Color fondoOscuro = new Color(30, 34, 38);
        Color panelGris = new Color(43, 48, 54);
        Color textoClaro = new Color(220, 225, 230);
        Color amarilloAlerta = new Color(241, 196, 15);
        Color verdeConsola = new Color(46, 204, 113);

        // Panel Contenedor Principal
        JPanel panelContenedor = new JPanel(new BorderLayout(15, 15));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenedor.setBackground(fondoOscuro);

        // --- ZONA SUPERIOR: FORMULARIO Y BOTONES ---
        JPanel panelNorte = new JPanel(new BorderLayout(10, 15));
        panelNorte.setBackground(fondoOscuro);

        // 1. Cuadrícula de Datos
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 15, 15));
        panelInput.setBackground(panelGris);

        TitledBorder bordeInput = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(amarilloAlerta, 1), " DATOS DE TELEMETRÍA ");
        bordeInput.setTitleFont(new Font("Consolas", Font.BOLD, 14));
        bordeInput.setTitleColor(amarilloAlerta);
        panelInput.setBorder(BorderFactory.createCompoundBorder(bordeInput, BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        Font fuenteLabel = new Font("SansSerif", Font.BOLD, 13);

        panelInput.add(crearLabel("Kilometraje Actual (km):", fuenteLabel, textoClaro));
        txtKm = crearTextField(fondoOscuro, textoClaro);
        panelInput.add(txtKm);

        panelInput.add(crearLabel("Carga Útil Promedio (kg):", fuenteLabel, textoClaro));
        txtCarga = crearTextField(fondoOscuro, textoClaro);
        panelInput.add(txtCarga);

        panelInput.add(crearLabel("Tiempo en Ralentí (Horas):", fuenteLabel, textoClaro));
        txtRalenti = crearTextField(fondoOscuro, textoClaro);
        panelInput.add(txtRalenti);

        panelInput.add(crearLabel("Temperatura Operativa (°C):", fuenteLabel, textoClaro));
        txtTemp = crearTextField(fondoOscuro, textoClaro);
        panelInput.add(txtTemp);

        panelInput.add(crearLabel("Tipo de Topografía:", fuenteLabel, textoClaro));
        String[] opciones = {"Plana", "Ondulada", "Montañosa"};
        cbTopografia = new JComboBox<>(opciones);
        cbTopografia.setFont(new Font("SansSerif", Font.BOLD, 13));
        cbTopografia.setBackground(Color.WHITE);
        cbTopografia.setForeground(Color.BLACK);
        panelInput.add(cbTopografia);

        // 2. Panel de Botones (Calcular y Exportar)
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBoton.setBackground(fondoOscuro);

        // Botón Diagnóstico
        JButton btnCalcular = new JButton("EJECUTAR DIAGNÓSTICO");
        btnCalcular.setPreferredSize(new Dimension(300, 45));
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCalcular.setBackground(amarilloAlerta);
        btnCalcular.setForeground(Color.BLACK);
        btnCalcular.setFocusPainted(false);
        btnCalcular.setContentAreaFilled(true);
        btnCalcular.setOpaque(true);
        btnCalcular.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Nuevo Botón Exportar (Estilo homologado)
        JButton btnExportar = new JButton("GUARDAR INFORME (.TXT)");
        btnExportar.setPreferredSize(new Dimension(300, 45));
        btnExportar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExportar.setBackground(amarilloAlerta); // Mismo fondo
        btnExportar.setForeground(Color.BLACK);    // Misma letra negra
        btnExportar.setFocusPainted(false);
        btnExportar.setContentAreaFilled(true);
        btnExportar.setOpaque(true);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBoton.add(btnCalcular);
        panelBoton.add(btnExportar);

        // Ensamblar panel superior
        panelNorte.add(panelInput, BorderLayout.CENTER);
        panelNorte.add(panelBoton, BorderLayout.SOUTH);

        // --- ZONA INFERIOR: RESULTADOS ---
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBackground(panelGris);

        TitledBorder bordeResultados = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(amarilloAlerta, 1), " CONSOLA DEL SISTEMA ");
        bordeResultados.setTitleFont(new Font("Consolas", Font.BOLD, 14));
        bordeResultados.setTitleColor(amarilloAlerta);
        panelResultados.setBorder(BorderFactory.createCompoundBorder(bordeResultados, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaResultado.setBackground(new Color(15, 18, 20));
        areaResultado.setForeground(verdeConsola);
        areaResultado.setCaretColor(Color.WHITE);
        areaResultado.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panelResultados.add(scroll, BorderLayout.CENTER);

        // Ensamblar todo
        panelContenedor.add(panelNorte, BorderLayout.NORTH);
        panelContenedor.add(panelResultados, BorderLayout.CENTER);

        getContentPane().setBackground(fondoOscuro);
        add(panelContenedor);

        // --- ACCIONES DE LOS BOTONES ---
        btnCalcular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarLogica();
            }
        });

        // Lógica para exportar el archivo de texto formal
        btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarInforme();
            }
        });
    }

    private JLabel crearLabel(String texto, Font fuente, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(color);
        return label;
    }

    private JTextField crearTextField(Color fondo, Color texto) {
        JTextField txt = new JTextField();
        txt.setBackground(fondo);
        txt.setForeground(texto);
        txt.setCaretColor(texto);
        txt.setFont(new Font("Consolas", Font.BOLD, 14));
        txt.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        return txt;
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

            String alertaFrenos = gestor.evaluarEstadoComponente("Vehículo", "Pastillas Frenos", 20000.0, op);
            String alertaAceite = gestor.evaluarEstadoComponente("Vehículo", "Aceite Motor", 5000.0, op);
            String alertaFiltros = gestor.evaluarEstadoComponente("Vehículo", "Filtros de Aire", 10000.0, op);

            StringBuilder reporteActual = new StringBuilder();
            reporteActual.append("==================================================\n");
            reporteActual.append(" INICIO DE ESCANEO PREDICTIVO...\n");
            reporteActual.append("==================================================\n");
            reporteActual.append(String.format(" >> Factor de estrés calculado: %.2fx\n\n", op.calcularMultiplicadorDesgaste()));
            reporteActual.append(estimador.obtenerEstimacionCambios(op));

            if (!alertaAceite.isEmpty() || !alertaFrenos.isEmpty() || !alertaFiltros.isEmpty()) {
                reporteActual.append("\n [!!!] ALERTAS CRÍTICAS DE SISTEMA [!!!]\n");
                if (!alertaAceite.isEmpty()) reporteActual.append(" * ").append(alertaAceite).append("\n");
                if (!alertaFrenos.isEmpty()) reporteActual.append(" * ").append(alertaFrenos).append("\n");
                if (!alertaFiltros.isEmpty()) reporteActual.append(" * ").append(alertaFiltros).append("\n");
            }
            reporteActual.append("\n");

            historial.agregarRegistro(reporteActual.toString());

            areaResultado.setText(historial.obtenerHistorialCompleto());
            areaResultado.setCaretPosition(areaResultado.getDocument().getLength());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "ERROR DE SENSOR: Los campos de telemetría solo admiten valores numéricos.",
                    "Fallo en la lectura de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarInforme() {
        if (areaResultado.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ningún diagnóstico generado para exportar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Informe Oficial");
        fileChooser.setSelectedFile(new File("Informe_Mantenimiento_Flota.txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String ruta = fileToSave.getAbsolutePath();

            if(!ruta.toLowerCase().endsWith(".txt")) {
                ruta += ".txt";
            }

            try (FileWriter fw = new FileWriter(ruta)) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                // Formato Oficial para el archivo de texto
                fw.write("*************************************************************\n");
                fw.write("           REPORTE OFICIAL DE ESTADO Y MANTENIMIENTO         \n");
                fw.write("*************************************************************\n");
                fw.write("Fecha de Emisión: " + dtf.format(now) + "\n");
                fw.write("Sistema: Motor de Predicción de Desgaste V2.5\n");
                fw.write("Auditor/Operador Responsable: Departamento de Mantenimiento\n");
                fw.write("-------------------------------------------------------------\n\n");

                // Imprimir el contenido de la consola
                fw.write(areaResultado.getText());

                fw.write("\n-------------------------------------------------------------\n");
                fw.write("FIN DEL INFORME. Documento generado automáticamente.\n");
                fw.write("*************************************************************\n");

                JOptionPane.showMessageDialog(this, "El informe ha sido guardado exitosamente.", "Archivo Exportado", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al escribir el archivo de texto.", "Error del Sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}