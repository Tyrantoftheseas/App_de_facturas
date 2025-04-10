package GUI;

import Loginapp.GestionFacturas;
import model.Cliente;
import model.Curso;
import DAO.CursoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class CursosGUI extends JFrame {
    private final CursoDAO cursoDAO;
    private final GestionFacturas gestionFacturas;
    private final Cliente cliente;
    private JList<String> listaCursos;
    private DefaultListModel<String> modeloCursos;
    private List<Curso> cursosDisponibles;

    public CursosGUI(Connection connection, GestionFacturas gestionFacturas, Cliente cliente) {
        this.cursoDAO = new CursoDAO(connection);
        this.gestionFacturas = gestionFacturas;
        this.cliente = cliente;

        inicializarGUI();
        cargarCursos();
    }

    private void inicializarGUI() {
        setTitle("Gestión de Cursos - Edutec");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel para el título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tituloLabel = new JLabel("Cursos Disponibles");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(tituloLabel);

        // Panel para la lista de cursos
        modeloCursos = new DefaultListModel<>();
        listaCursos = new JList<>(modeloCursos);
        JScrollPane scrollPane = new JScrollPane(listaCursos);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnInscribir = new JButton("Inscribirse");
        JButton btnDetalles = new JButton("Ver Detalles");
        JButton btnVolver = new JButton("Volver");

        // Configurar los botones
        btnInscribir.addActionListener(e -> inscribirEnCurso());
        btnDetalles.addActionListener(e -> verDetallesCurso());
        btnVolver.addActionListener(e -> dispose());

        panelBotones.add(btnInscribir);
        panelBotones.add(btnDetalles);
        panelBotones.add(btnVolver);

        // Añadir todos los paneles al frame principal
        add(panelTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCursos() {
        modeloCursos.clear();
        cursosDisponibles = cursoDAO.obtenerTodosCursos();

        if (cursosDisponibles.isEmpty()) {
            // Si no hay cursos en la base de datos, crear algunos de ejemplo
            Curso programacion = new Curso(1, "Programación Java", 299.99);
            Curso diseno = new Curso(2, "Diseño UX/UI", 249.99);
            Curso marketing = new Curso(3, "Marketing Digital", 199.99);

            cursosDisponibles.add(programacion);
            cursosDisponibles.add(diseno);
            cursosDisponibles.add(marketing);
        }

        // Llenar el modelo con los cursos disponibles
        for (Curso curso : cursosDisponibles) {
            modeloCursos.addElement(curso.getNombre() + " - $" + curso.getPrecio());
        }
    }

    private void inscribirEnCurso() {
        int selectedIndex = listaCursos.getSelectedIndex();
        if (selectedIndex != -1) {
            Curso cursoSeleccionado = cursosDisponibles.get(selectedIndex);
            gestionFacturas.generarFactura(cliente, cursoSeleccionado);
            JOptionPane.showMessageDialog(this,
                    "Factura generada correctamente para el curso: " + cursoSeleccionado.getNombre(),
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un curso primero",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void verDetallesCurso() {
        int selectedIndex = listaCursos.getSelectedIndex();
        if (selectedIndex != -1) {
            Curso cursoSeleccionado = cursosDisponibles.get(selectedIndex);
            JOptionPane.showMessageDialog(this,
                    "Nombre: " + cursoSeleccionado.getNombre() + "\n" +
                            "Precio: $" + cursoSeleccionado.getPrecio() + "\n" +
                            "Descripción: " + cursoSeleccionado.getDescripcion(),
                    "Detalles del Curso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un curso primero",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}