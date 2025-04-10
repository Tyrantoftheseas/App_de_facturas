package Loginapp;

import GUI.CursosGUI;
import GUI.FacturasGUI;
import GUI.PagosGUI;
import model.Cliente;
import DAO.CursoDAO;
import DAO.FacturaDAO;
import DAO.PagoDAO;

import java.awt.*;
import java.sql.*;
import java.time.Instant;
import javax.swing.*;

public class LoginApp {
    private static JFrame loginFrame;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutec_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private final Connection connection;

    public LoginApp(Connection connection) {
        this.connection = connection;
    }

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static void mostrarLogin(Connection connect) {
        Connection conn = connect();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.");
            return;
        }

        loginFrame = new JFrame("Login Edutec");
        loginFrame.setSize(350, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.getContentPane().setBackground(new Color(200, 220, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Correo:");
        JTextField txtUser = new JTextField(15);
        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField txtPass = new JPasswordField(15);
        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrarse");
        JLabel lblMessage = new JLabel("", SwingConstants.CENTER);

        btnLogin.setBackground(new Color(100, 150, 200));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);

        btnRegister.setBackground(new Color(120, 170, 220));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(lblUser, gbc);
        gbc.gridx = 1;
        loginFrame.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginFrame.add(lblPass, gbc);
        gbc.gridx = 1;
        loginFrame.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginFrame.add(btnLogin, gbc);

        gbc.gridy = 3;
        loginFrame.add(btnRegister, gbc);

        gbc.gridy = 4;
        loginFrame.add(lblMessage, gbc);

        btnLogin.addActionListener(e -> {
            Cliente cliente = validarUsuario(conn, txtUser.getText(), new String(txtPass.getPassword()));
            if (cliente != null) {
                loginFrame.dispose();
                mostrarMenuPrincipal(cliente, conn);
            } else {
                lblMessage.setText("Credenciales incorrectas");
                lblMessage.setForeground(Color.RED);
            }
        });

        btnRegister.addActionListener(e -> {
            loginFrame.setVisible(false);
            new RegistroUsuario(conn); // Pasar la conexión al registro
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void mostrarMenuPrincipal(Cliente cliente, Connection conn) {
        JFrame menuFrame = new JFrame("Panel Principal - Edutec");
        menuFrame.setSize(600, 500);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 0, 15));
        JPanel backgroundPanel = new JPanel();
        JPanel footerPanel = new JPanel();

        CursoDAO cursoDAO = new CursoDAO(conn);
        FacturaDAO facturaDAO = new FacturaDAO(conn);
        PagoDAO pagoDAO = new PagoDAO(conn);

        GestionFacturas gestionFacturas = new GestionFacturas(facturaDAO);
        GestionCursos gestionCursos = new GestionCursos(conn, gestionFacturas);

        JButton btnGestionPagos = new JButton("Gestión de Pagos");
        btnGestionPagos.addActionListener(e -> {
            PagosGUI pagosGUI = new PagosGUI(cliente, conn, pagoDAO);
            pagosGUI.setVisible(true);
        });

        JButton btnClientes = new JButton("Administrar Clientes");
        btnClientes.addActionListener(e -> JOptionPane.showMessageDialog(menuFrame,
                "Funcionalidad de administración de clientes aún no implementada."));

        JButton btnCursos = new JButton("Gestionar Cursos");
        btnCursos.addActionListener(e -> {
            CursosGUI cursosGUI = new CursosGUI(conn, gestionFacturas, cliente);
            cursosGUI.setVisible(true);
        });

        JButton btnFacturas = new JButton("Gestionar Facturas");
        btnFacturas.addActionListener(e -> {
            FacturasGUI facturasGUI = new FacturasGUI(cliente, facturaDAO);
            facturasGUI.setVisible(true);
        });

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> {
            menuFrame.dispose();
            mostrarLogin(conn);
        });

        buttonPanel.add(btnGestionPagos);
        buttonPanel.add(btnClientes);
        buttonPanel.add(btnCursos);
        buttonPanel.add(btnFacturas);
        buttonPanel.add(btnLogout);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        menuFrame.add(headerPanel, BorderLayout.NORTH);
        menuFrame.add(backgroundPanel, BorderLayout.CENTER);
        menuFrame.add(footerPanel, BorderLayout.SOUTH);

        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    public static Cliente validarUsuario(Connection conn, String correo, String contrasena) {
        if (correo == null || contrasena == null || correo.isEmpty() || contrasena.isEmpty()) {
            System.err.println("Correo o contraseña vacíos.");
            return null;
        }

        String query = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                return new Cliente(
                        id,
                        rs.getString("nombre") + " " + rs.getString("apellido"),
                        correo,
                        rs.getString("telefono") != null ? rs.getString("telefono") : "No disponible"
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }
        return null;
    }
}