package Loginapp;
import model.Cliente;
import model.Curso;
import DAO.CursoDAO; // Importa el DAO
import Loginapp.GestionFacturas; // Asegúrate de que la importación sea correcta
import java.sql.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GestionCursos {
    private final CursoDAO cursoDAO;
    private final GestionFacturas gestionFacturas;

    public GestionCursos(Connection connection, GestionFacturas gestionFacturas) {
        this.cursoDAO = new CursoDAO(connection);
        this.gestionFacturas = gestionFacturas;
    }

    public void mostrarGestionCursos(Cliente cliente) {
        JFrame cursosFrame = new JFrame("Gestión de Cursos - Edutec");
        cursosFrame.setSize(500, 400);
        cursosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cursosFrame.setLayout(new BorderLayout());

        // Panel para la lista de cursos
        JPanel panelLista = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Cursos Disponibles", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Obtener los cursos desde la base de datos usando el DAO
        List<Curso> cursosDisponibles = cursoDAO.obtenerTodosCursos();
        DefaultListModel<String> modeloCursos = new DefaultListModel<>();

        // Si la base de datos está vacía o hay un problema,
        // puedes usar los cursos de ejemplo como respaldo (opcional)
        if (cursosDisponibles.isEmpty()) {
            // Crear algunos cursos de ejemplo (ESTOS SON LOS QUE PEDISTE)
            Curso programacion = new Curso(1, "Programación Java", 299.99);
            Curso diseno = new Curso(2, "Diseño UX/UI", 249.99);
            Curso marketing = new Curso(3, "Marketing Digital", 199.99);

            modeloCursos.addElement(programacion.getNombre() + " - $" + programacion.getPrecio());
            modeloCursos.addElement(diseno.getNombre() + " - $" + diseno.getPrecio());
            modeloCursos.addElement(marketing.getNombre() + " - $" + marketing.getPrecio());
            cursosDisponibles.add(programacion); // Añadir a la lista para usar en el evento
            cursosDisponibles.add(diseno);
            cursosDisponibles.add(marketing);
        } else {
            for (Curso curso : cursosDisponibles) {
                modeloCursos.addElement(curso.getNombre() + " - $" + curso.getPrecio());
            }
        }

        JList<String> listaCursos = new JList<>(modeloCursos);
        JScrollPane scrollPane = new JScrollPane(listaCursos);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnInscribir = new JButton("Inscribirse");
        JButton btnDetalles = new JButton("Ver Detalles");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnInscribir);
        panelBotones.add(btnDetalles);
        panelBotones.add(btnVolver);

        // Evento para inscribirse a un curso
        btnInscribir.addActionListener(e -> {
            int selectedIndex = listaCursos.getSelectedIndex();
            if (selectedIndex != -1) {
                Curso cursoSeleccionado = cursosDisponibles.get(selectedIndex);
                gestionFacturas.generarFactura(cliente, cursoSeleccionado);
            } else {
                JOptionPane.showMessageDialog(cursosFrame,
                        "Por favor, seleccione un curso primero",
                        "Selección requerida",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> cursosFrame.dispose());

        panelLista.add(titleLabel, BorderLayout.NORTH);
        panelLista.add(scrollPane, BorderLayout.CENTER);

        cursosFrame.add(panelLista, BorderLayout.CENTER);
        cursosFrame.add(panelBotones, BorderLayout.SOUTH);
        cursosFrame.setLocationRelativeTo(null);
        cursosFrame.setVisible(true);
    }
}
