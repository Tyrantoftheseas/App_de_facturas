package Loginapp;
import model.Cliente;
import model.Curso;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GestionCursos {
    // Metodo para mostrar la gestión de cursos
    public static void mostrarGestionCursos(Cliente cliente) {
        JFrame cursosFrame = new JFrame("Gestión de Cursos - Edutec");
        cursosFrame.setSize(500, 400);
        cursosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cursosFrame.setLayout(new BorderLayout());

        // Panel para la lista de cursos
        JPanel panelLista = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Cursos Disponibles", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Simulación de cursos (en una aplicación real, estos vendrían de la base de datos)
        DefaultListModel<String> modeloCursos = new DefaultListModel<>();

        // Crear algunos cursos de ejemplo
        Curso programacion = new Curso(1, "Programación Java", 299.99);
        Curso diseno = new Curso(2, "Diseño UX/UI", 249.99);
        Curso marketing = new Curso(3, "Marketing Digital", 199.99);

        modeloCursos.addElement(programacion.getNombre() + " - $" + programacion.getPrecio());
        modeloCursos.addElement(diseno.getNombre() + " - $" + diseno.getPrecio());
        modeloCursos.addElement(marketing.getNombre() + " - $" + marketing.getPrecio());

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
                Curso cursoSeleccionado = null;
                switch (selectedIndex) {
                    case 0: cursoSeleccionado = programacion; break;
                    case 1: cursoSeleccionado = diseno; break;
                    case 2: cursoSeleccionado = marketing; break;
                }

                if (cursoSeleccionado != null) {
                    GestionFacturas.generarFactura(cliente, cursoSeleccionado);
                }
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