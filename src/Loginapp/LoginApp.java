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
        loginFrame.setSize(400, 420);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.getContentPane().setBackground(Color.decode("#64e8e8"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(10, 10, 10, 10);
        pgbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblBienvenida = new JLabel("Inicia sesión en Edutec", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(new Color(33, 150, 243));

        JLabel lblUser = new JLabel("Correo:");
        JTextField txtUser = new JTextField(15);
        lblUser.setFont(new Font("Times New Roman", Font.BOLD, 14));

        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField txtPass = new JPasswordField(15);
        lblPass.setFont(new Font("Times New Roman", Font.BOLD, 14));

        JButton btnLogin = new JButton("Iniciar Sesión");
        JButton btnRegister = new JButton("Registrarse");
        JLabel lblMessage = new JLabel("", SwingConstants.CENTER);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        txtUser.setFont(fieldFont);
        txtPass.setFont(fieldFont);

        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.BLUE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);

        btnRegister.setBackground(new Color(100, 181, 246));
        btnRegister.setForeground(Color.BLUE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setFocusPainted(false);

        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        pgbc.gridx = 0; pgbc.gridy = 0; pgbc.gridwidth = 2;
        panel.add(lblBienvenida, pgbc);

        pgbc.gridy++;
        panel.add(lblUser, pgbc);
        pgbc.gridy++;
        panel.add(txtUser, pgbc);

        pgbc.gridy++;
        panel.add(lblPass, pgbc);
        pgbc.gridy++;
        panel.add(txtPass, pgbc);

        pgbc.gridy++;
        panel.add(btnLogin, pgbc);

        pgbc.gridy++;
        panel.add(btnRegister, pgbc);

        pgbc.gridy++;
        panel.add(lblMessage, pgbc);

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(panel, gbc);

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

        // Panel superior para encabezado
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#64e8e8"));
        headerPanel.setPreferredSize(new Dimension(600, 60));
        JLabel titleLabel = new JLabel("Sistema de Gestión Edutec", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        headerPanel.add(titleLabel);

        // Panel central con botones
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 0, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Panel de pie de página
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.decode("#e0e0e0"));
        footerPanel.setPreferredSize(new Dimension(600, 30));
        JLabel userLabel = new JLabel("Usuario: " + cliente.getNombre(), SwingConstants.CENTER);
        userLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerPanel.add(userLabel);

        CursoDAO cursoDAO = new CursoDAO(conn);
        FacturaDAO facturaDAO = new FacturaDAO(conn);
        PagoDAO pagoDAO = new PagoDAO(conn);

        GestionFacturas gestionFacturas = new GestionFacturas(facturaDAO);
        GestionCursos gestionCursos = new GestionCursos(conn, gestionFacturas);

        // Estilo para botones
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Color buttonColor = new Color(33, 150, 243);
        Color textColor = Color.WHITE;

        JButton btnGestionPagos = new JButton("Gestión de Pagos");
        styleButton(btnGestionPagos, buttonFont, buttonColor, textColor);
        btnGestionPagos.addActionListener(e -> {
            PagosGUI pagosGUI = new PagosGUI(cliente, conn, pagoDAO);
            pagosGUI.setVisible(true);
        });

        JButton btnClientes = new JButton("Administrar Clientes");
        styleButton(btnClientes, buttonFont, buttonColor, textColor);
        btnClientes.addActionListener(e -> JOptionPane.showMessageDialog(menuFrame,
                "Funcionalidad de administración de clientes aún no implementada."));

        JButton btnCursos = new JButton("Gestionar Cursos");
        styleButton(btnCursos, buttonFont, buttonColor, textColor);
        btnCursos.addActionListener(e -> {
            CursosGUI cursosGUI = new CursosGUI(conn, gestionFacturas, cliente);
            cursosGUI.setVisible(true);
        });

        JButton btnFacturas = new JButton("Gestionar Facturas");
        styleButton(btnFacturas, buttonFont, buttonColor, textColor);
        btnFacturas.addActionListener(e -> {
            FacturasGUI facturasGUI = new FacturasGUI(cliente, facturaDAO);
            facturasGUI.setVisible(true);
        });

        JButton btnLogout = new JButton("Cerrar Sesión");
        styleButton(btnLogout, buttonFont, new Color(244, 67, 54), textColor);
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

    private static void styleButton(JButton button, Font font, Color bgColor, Color fgColor) {
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Connection conn = connect();
            mostrarLogin(conn);
        });
    }
}