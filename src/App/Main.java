package App;

import Loginapp.GestionFacturas;
import Loginapp.LoginApp;
import model.Cliente;
import DAO.CursoDAO;
import DAO.FacturaDAO;
import DAO.PagoDAO;
import GUI.CursosGUI;
import GUI.FacturasGUI;
import GUI.PagosGUI;
import GUI.HistorialFacturasGUI;
import GUI.HistorialPagosGUI;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Connection conn = LoginApp.connect();
            if (conn != null) {
                LoginApp.mostrarLogin(conn);
            } else {
                mostrarErrorYSalir("Error al conectar a la base de datos. La aplicación se cerrará.");
            }
        });
    }

    public static void mostrarMenuPrincipal(Cliente cliente, Connection conn) {
        JFrame menuFrame = crearVentana("Panel Principal - Edutec", 900, 500);
        menuFrame.getContentPane().setBackground(new Color(220, 240, 255));

        // Panel superior con logo
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(new Color(0, 70, 150));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel lblLogo = new JLabel("Edutec", SwingConstants.CENTER);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        logoPanel.add(lblLogo, BorderLayout.CENTER);
        menuFrame.add(logoPanel, BorderLayout.NORTH);

        // Título de bienvenida
        JLabel titulo = crearTitulo("Bienvenido a Edutec, " + cliente.getNombre());
        menuFrame.add(titulo, BorderLayout.WEST);

        // Panel central con botones dispuestos horizontalmente
        JPanel buttonPanel = crearPanelBotones(cliente, conn, menuFrame);
        menuFrame.add(buttonPanel, BorderLayout.CENTER);

        // Pie de página
        JLabel footer = crearFooter("Sistema Edutec \\- Desarrollado por George");
        menuFrame.add(footer, BorderLayout.SOUTH);

        menuFrame.setVisible(true);
    }

    // Se usa GridLayout de 1 fila para organizar botones horizontalmente
    private static JPanel crearPanelBotones(Cliente cliente, Connection conn, JFrame menuFrame) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(220, 240, 255));

        FacturaDAO facturaDAO = new FacturaDAO(conn);
        PagoDAO pagoDAO = new PagoDAO(conn);
        CursoDAO cursoDAO = new CursoDAO(conn);

        buttonPanel.add(crearBoton("Gestión de Pagos", e -> new PagosGUI(cliente, conn, pagoDAO).setVisible(true)));
        buttonPanel.add(crearBoton("Administrar Clientes", e -> mostrarMensaje("Funcionalidad aún no implementada.")));
        buttonPanel.add(crearBoton("Gestionar Cursos", e -> new CursosGUI(conn, new GestionFacturas(facturaDAO), cliente).setVisible(true)));
        buttonPanel.add(crearBoton("Gestionar Facturas", e -> new FacturasGUI(cliente, facturaDAO).setVisible(true)));
        buttonPanel.add(crearBoton("Historial de Pagos", e -> new HistorialPagosGUI(cliente, conn, pagoDAO).setVisible(true)));
        buttonPanel.add(crearBoton("Historial de Facturas", e -> new HistorialFacturasGUI(cliente, conn, facturaDAO).setVisible(true)));
        buttonPanel.add(crearBoton("Cerrar Sesión", e -> cerrarSesion(menuFrame)));

        return buttonPanel;
    }

    private static void cerrarSesion(JFrame menuFrame) {
        menuFrame.dispose();
        SwingUtilities.invokeLater(() -> {
            Connection newConn = LoginApp.connect();
            if (newConn != null) {
                LoginApp.mostrarLogin(newConn);
            } else {
                mostrarErrorYSalir("Error al conectar a la base de datos.");
            }
        });
    }

    private static JFrame crearVentana(String titulo, int ancho, int alto) {
        JFrame frame = new JFrame(titulo);
        frame.setSize(ancho, alto);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 20));
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static JLabel crearTitulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return titulo;
    }

    private static JLabel crearFooter(String texto) {
        JLabel footer = new JLabel(texto, SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return footer;
    }

    // Se ha aumentado el tamaño de los botones, se agrega un icono estándar y se organiza el texto debajo del icono
    private static JButton crearBoton(String texto, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(texto);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.addActionListener(actionListener);
        button.setFocusPainted(false);
        button.setBackground(new Color(180, 210, 255));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Tamaño mayor para los botones
        button.setPreferredSize(new Dimension(140, 100));
        // Asignar icono estándar y ajustar posición del texto
        Icon icon = UIManager.getIcon("OptionPane.informationIcon");
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        return button;
    }

    private static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    private static void mostrarErrorYSalir(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}