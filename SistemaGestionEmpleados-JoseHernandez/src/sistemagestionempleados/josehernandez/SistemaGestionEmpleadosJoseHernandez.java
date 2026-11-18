package sistemagestionempleados.josehernandez;
// todos los componentes que vamos a necesitar para nuestra table, color, fechas, cuadriculas, fuentes,combobox,cajas de dialogo acomodar paneles margenes  la venta principal
//programa el texto donde van a ingresar los datos, los comboxes de el tipo de empleado detectores de eventos
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class SistemaGestionEmpleadosJoseHernandez extends JFrame {
//formatos para mostrar fechas
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private GestorEmpleados gestor = new GestorEmpleados();
// texto cajas
    private JTextField txtNombre = new JTextField(12);
    private JTextField txtApellido = new JTextField(12);
    private JTextField txtCedula = new JTextField(12);
    private JTextField txtHijos = new JTextField(12);
//info del combo box
    private JComboBox<String> cmbTipo = new JComboBox<>(new String[]{
        "Empleado por Hora",
        "Empleado Temporal",
        "Empleado Planta Permanente"
    });
// campos dinamicos
    private JLabel lblExtra1 = new JLabel("Horas trabajadas en el mes:");
    private JTextField txtExtra1 = new JTextField(12);
    private JButton btnCalendario1 = new JButton("...");

    private JLabel lblExtra2 = new JLabel("Fecha fin contrato:");
    private JTextField txtExtra2 = new JTextField(12);
    private JButton btnCalendario2 = new JButton("...");
// botones
    private JButton btnGuardar = new JButton("Guardar");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnEliminar = new JButton("Eliminar");
    
// tabla para mostra empleados
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public SistemaGestionEmpleadosJoseHernandez() {
        setTitle("Sistema de Gestión de Empleados");
        setSize(900, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(4, 4, 8, 8));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 12, 6, 12));
//fila 1
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellido);
//fila2
        panelForm.add(new JLabel("Cédula:"));
        panelForm.add(txtCedula);
        panelForm.add(new JLabel("Cantidad de hijos:"));
        panelForm.add(txtHijos);
//fila 3
        panelForm.add(new JLabel("Tipo de empleado:"));
        panelForm.add(cmbTipo);
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));
//paneles de fechas auxiliares
        JPanel panelCampoExtra1 = crearPanelConSelectorFecha(txtExtra1, btnCalendario1);
        JPanel panelCampoExtra2 = crearPanelConSelectorFecha(txtExtra2, btnCalendario2);
//etiquetas y panles del formulario
        panelForm.add(lblExtra1);
        panelForm.add(panelCampoExtra1);
        panelForm.add(lblExtra2);
        panelForm.add(panelCampoExtra2);
//alineacion de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 12));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
//panel que une formulario norte con botones abajo
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelForm, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
//columnas modelo de la table no sean editables 
       String[] columnas = {"Nombre", "Apellido", "Cédula", "Hijos", "Tipo", "Detalle", "Sueldo (Bs.)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
//creacion de tabla y que solo se pueda seleccionar uno
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(22);
//tabla con scroll y la ubicamos en el centro
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
//filtros y restricciones
        configurarFiltrosPorTipo();
        configurarRestriccionesTeclado();
//evento 1 si cambia el combobox
        cmbTipo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                configurarFiltrosPorTipo();
            }
        });
//evento abrir mini calendario si el empleado es temporal
        btnCalendario1.addActionListener(e -> abrirMiniCalendario(txtExtra1));
        btnCalendario2.addActionListener(e -> abrirMiniCalendario(txtExtra2));

        txtExtra1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Empleado Temporal".equals(cmbTipo.getSelectedItem()) && txtExtra1.isEnabled()) {
                    abrirMiniCalendario(txtExtra1);
                }
            }
        });

        txtExtra2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Empleado Temporal".equals(cmbTipo.getSelectedItem()) && txtExtra2.isEnabled()) {
                    abrirMiniCalendario(txtExtra2);
                }
            }
        });
//funciones de los botones
        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
    }
//union del boton del mini calendario con el texto
    private JPanel crearPanelConSelectorFecha(JTextField campo, JButton boton) {
        JPanel panel = new JPanel(new BorderLayout(4, 0));
        panel.add(campo, BorderLayout.CENTER);
        panel.add(boton, BorderLayout.EAST);
        return panel;
    }

    private void abrirMiniCalendario(JTextField campoDestino) {
        MiniCalendarioDialog dialogo = new MiniCalendarioDialog(this, campoDestino.getText().trim());
        dialogo.setVisible(true);
        String fechaSeleccionada = dialogo.getFechaSeleccionada();
        if (fechaSeleccionada != null) {
            campoDestino.setText(fechaSeleccionada);
        }
    }
// restricciones en las cajas de texto
    private void configurarRestriccionesTeclado() {
        txtNombre.addKeyListener(new KeyAdapter() {
            @Override
            //solo letras espacio y borrar
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
//solo letras
        txtApellido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
//solo numeros
        txtCedula.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
// numeros
        txtHijos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }
// filtros por tipo adata campos segun el tipo seleccionado
    private void configurarFiltrosPorTipo() {
        String tipo = (String) cmbTipo.getSelectedItem();
//limpia campos extra
        txtExtra1.setText("");
        txtExtra2.setText("");
//config empleado por hora
        if ("Empleado por Hora".equals(tipo)) {
            txtHijos.setText("0");
            txtHijos.setEnabled(false);
//habilita horas y tapa calendario
            lblExtra1.setText("Horas trabajadas en el mes:");
            txtExtra1.setEditable(true);
            txtExtra1.setEnabled(true);
            btnCalendario1.setVisible(false);
//deshabilita lo del contrato
            lblExtra2.setText("Fecha fin contrato:");
            txtExtra2.setEditable(true);
            txtExtra2.setEnabled(false);
            btnCalendario2.setVisible(false);
         //   inicio de config para empleqado temporal
        } else if ("Empleado Temporal".equals(tipo)) {
            if ("0".equals(txtHijos.getText().trim())) {
                txtHijos.setText("");
            }
            txtHijos.setEnabled(true);

            lblExtra1.setText("Fecha ingreso:");
            txtExtra1.setEditable(false);
            txtExtra1.setEnabled(true);
            btnCalendario1.setVisible(true);
//fecha de contratos calendario
            lblExtra2.setText("Fecha fin contrato:");
            txtExtra2.setEditable(false);
            txtExtra2.setEnabled(true);
            btnCalendario2.setVisible(true);
        } else {
            //config empleado de planta
            if ("0".equals(txtHijos.getText().trim())) {
                txtHijos.setText("");
            }
            txtHijos.setEnabled(true);
//antiguedad y oculta calendario
            lblExtra1.setText("Antigüedad (años):");
            txtExtra1.setEditable(true);
            txtExtra1.setEnabled(true);
            btnCalendario1.setVisible(false);

            lblExtra2.setText("Fecha fin contrato:");
            txtExtra2.setEditable(true);
            txtExtra2.setEnabled(false);
            btnCalendario2.setVisible(false);
        }

        revalidate();
        repaint();
    }
//validaciones de texto
    private boolean esTextoValido(String valor) {
        return valor != null && valor.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }
//validacion numero
    private boolean esNumeroValido(String valor) {
        return valor != null && valor.matches("\\d+");
    }
//validacion fecha
    private boolean esFechaValida(String texto) {
        try {
            LocalDate.parse(texto, FORMATO_FECHA);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
//guardar empleado dialogo de texto
    private void guardarEmpleado() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar la información?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
// guarda y limpia los campos basicos
        try {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String cedula = txtCedula.getText().trim();
//valida nombre
            if (!esTextoValido(nombre)) {
                JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras.");
                return;
            }
            //validamos apellido
            if (!esTextoValido(apellido)) {
                JOptionPane.showMessageDialog(this, "El apellido solo debe contener letras.");
                return;
            }
            //validamos cedula
            if (!esNumeroValido(cedula) || cedula.length() < 6) {
                JOptionPane.showMessageDialog(this, "La cédula debe contener solo números válidos.");
                return;
            }

            String tipo = (String) cmbTipo.getSelectedItem();
            Empleado emp;
//leemos el tipo de empleado
// validamos y calculamos
            if ("Empleado por Hora".equals(tipo)) {
                String horasTexto = txtExtra1.getText().trim();
                if (!esNumeroValido(horasTexto)) {
                    JOptionPane.showMessageDialog(this, "Ingresa la cantidad de horas trabajadas durante el mes.");
                    return;
                }
                //validacion maximo de hora trabajado
                int horasMes = Integer.parseInt(horasTexto);
                if (horasMes <= 0 || horasMes > 744) {
                    JOptionPane.showMessageDialog(this, "Las horas trabajadas en el mes deben estar entre 1 y 744.");
                    return;
                }
                
                emp = new EmpleadoPorHora(nombre, apellido, cedula, horasMes);
                //validacion empleado temporal
            } else if ("Empleado Temporal".equals(tipo)) {
                String hijosTexto = txtHijos.getText().trim();
                String ingreso = txtExtra1.getText().trim();
                String fin = txtExtra2.getText().trim();
                if (!esNumeroValido(hijosTexto) || !esFechaValida(ingreso) || !esFechaValida(fin)) {
                    JOptionPane.showMessageDialog(this, "Completa hijos y selecciona ambas fechas en el calendario.");
                    return;
                }
                int hijos = Integer.parseInt(hijosTexto);
                LocalDate fechaIngreso = LocalDate.parse(ingreso, FORMATO_FECHA);
                LocalDate fechaFin = LocalDate.parse(fin, FORMATO_FECHA);
                if (fechaFin.isBefore(fechaIngreso)) {
                    JOptionPane.showMessageDialog(this, "La fecha fin de contrato no puede ser anterior a la de ingreso.");
                    return;
                }
                //creamos la instancia empleado temporal con datos correctos
                emp = new EmpleadoTemporal(nombre, apellido, cedula, hijos, ingreso, fin);
            } else {
                // si es de planta validamos hijos y años
                String hijosTexto = txtHijos.getText().trim();
                String antiguedadTexto = txtExtra1.getText().trim();
                if (!esNumeroValido(hijosTexto) || !esNumeroValido(antiguedadTexto)) {
                    JOptionPane.showMessageDialog(this, "Hijos y antigüedad deben ser números válidos.");
                    return;
                }
                int hijos = Integer.parseInt(hijosTexto);
                int antiguedad = Integer.parseInt(antiguedadTexto);
                emp = new EmpleadoPermanente(nombre, apellido, cedula, hijos, antiguedad);
            }
// agrega empleado si esta lleno avisa
            if (!gestor.agregar(emp)) {
                JOptionPane.showMessageDialog(this, "No hay espacio disponible.");
                return;
            }
//nueva fila a tabla con los datos y el sueldo calculado
            modeloTabla.addRow(new Object[]{
                emp.getNombre(),
                emp.getApellido(),
                emp.getCedula(),
                emp.getTextoHijos(),
                emp.getTipoEmpleado(),
                emp.getDetalle(),
                emp.calcularSueldo()
            });
// vaciamos las cajas de texto
            limpiarCamposDirecto();
            JOptionPane.showMessageDialog(this, "Datos almacenados correctamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisa que los campos numéricos tengan solo números válidos.");
        }
    }
//boton limpiar confirmacion 
    private void limpiarFormulario() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de limpiar los datos?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            limpiarCamposDirecto();
        }
    }
//metodo de eliminar empleado en caso de no seleccionar avisa
    private void eliminarEmpleado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila de la tabla para eliminar.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el empleado seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmacion == JOptionPane.YES_OPTION && gestor.eliminar(fila)) {
            modeloTabla.removeRow(fila);
        }
    }
//limpia todo y devuelve a inicio
    private void limpiarCamposDirecto() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtHijos.setText("");
        txtExtra1.setText("");
        txtExtra2.setText("");
        cmbTipo.setSelectedIndex(0);
        configurarFiltrosPorTipo();
        txtNombre.requestFocusInWindow();
    }
// punto de entrada a la aplicacion
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaGestionEmpleadosJoseHernandez().setVisible(true);
        });
    }
// clase interna para implementar dialogos de fehca
    private static class MiniCalendarioDialog extends JDialog {

        private YearMonth mesActual;
        private String fechaSeleccionada;
        private JLabel lblMesAnio = new JLabel("", SwingConstants.CENTER);
        private JPanel panelDias = new JPanel(new GridLayout(0, 7, 4, 4));

        public MiniCalendarioDialog(JFrame padre, String fechaInicial) {
            super(padre, "Seleccionar fecha", true);
            setSize(320, 270);
            setLocationRelativeTo(padre);
            setLayout(new BorderLayout(8, 8));

            LocalDate base = LocalDate.now();
            if (fechaInicial != null && !fechaInicial.isEmpty()) {
                try {
                    base = LocalDate.parse(fechaInicial, FORMATO_FECHA);
                } catch (DateTimeParseException ignored) {
                }
            }
            mesActual = YearMonth.from(base);

            JButton btnAnterior = new JButton("<");
            JButton btnSiguiente = new JButton(">");
            btnAnterior.addActionListener(e -> {
                mesActual = mesActual.minusMonths(1);
                renderizarDias();
            });
            btnSiguiente.addActionListener(e -> {
                mesActual = mesActual.plusMonths(1);
                renderizarDias();
            });

            JPanel panelCabecera = new JPanel(new BorderLayout(5, 0));
            panelCabecera.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
            lblMesAnio.setFont(lblMesAnio.getFont().deriveFont(Font.BOLD));
            panelCabecera.add(btnAnterior, BorderLayout.WEST);
            panelCabecera.add(lblMesAnio, BorderLayout.CENTER);
            panelCabecera.add(btnSiguiente, BorderLayout.EAST);

            panelDias.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));

            add(panelCabecera, BorderLayout.NORTH);
            add(panelDias, BorderLayout.CENTER);

            renderizarDias();
        }

        private void renderizarDias() {
            panelDias.removeAll();
            lblMesAnio.setText(mesActual.getMonth().name() + " " + mesActual.getYear());

            String[] diasSemana = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
            for (String d : diasSemana) {
                JLabel lbl = new JLabel(d, SwingConstants.CENTER);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                panelDias.add(lbl);
            }

            LocalDate primerDia = mesActual.atDay(1);
            int desplazamiento = primerDia.getDayOfWeek().getValue() - 1;
            for (int i = 0; i < desplazamiento; i++) {
                panelDias.add(new JLabel(""));
            }

            int totalDias = mesActual.lengthOfMonth();
            for (int dia = 1; dia <= totalDias; dia++) {
                LocalDate fecha = mesActual.atDay(dia);
                JButton btnDia = new JButton(String.valueOf(dia));
                btnDia.setMargin(new Insets(2, 2, 2, 2));
                if (fecha.equals(LocalDate.now())) {
                    btnDia.setBackground(new Color(220, 235, 252));
                }
                btnDia.addActionListener(e -> {
                    fechaSeleccionada = fecha.format(FORMATO_FECHA);
                    dispose();
                });
                panelDias.add(btnDia);
            }

            panelDias.revalidate();
            panelDias.repaint();
        }

        public String getFechaSeleccionada() {
            return fechaSeleccionada;
        }
    }
}