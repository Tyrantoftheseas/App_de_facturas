package Loginapp;

import model.Cliente;
import java.awt.*;
import java.sql.*;
import java.time.Instant;
import javax.swing.*;

public class LoginApp {
    private static JFrame loginFrame;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutec_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
    public static void registrarNotificacion(Connection conn, int usuarioId, String tipo, String asunto, String mensaje) {
        String query = "INSERT INTO notificaciones (usuario_id, tipo, asunto, mensaje, fecha_envio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tipo);
            stmt.setString(3, asunto);
            stmt.setString(4, mensaje);
            stmt.setTimestamp(5, java.sql.Timestamp.from(Instant.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar notificación: " + e.getMessage());
        }
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

                actualizarUltimoAcceso(conn, id);
                notificarLoginExitoso(conn, id);

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

    private static void actualizarUltimoAcceso(Connection conn, int usuarioId) {
        String query = "UPDATE usuarios SET ultimo_acceso = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.from(Instant.now()));
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
        }
    }

    private static void notificarLoginExitoso(Connection conn, int usuarioId) {
        String query = "INSERT INTO notificaciones (usuario_id, tipo, asunto, mensaje, fecha_envio) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, "login");
            stmt.setString(3, "Inicio de sesión exitoso");
            stmt.setString(4, "Se ha detectado un inicio de sesión exitoso en su cuenta.");
            stmt.setTimestamp(5, Timestamp.from(Instant.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar notificación: " + e.getMessage());
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
                mostrarMenuPrincipal(cliente);
            } else {
                lblMessage.setText("Credenciales incorrectas");
                lblMessage.setForeground(Color.RED);
            }
        });

        btnRegister.addActionListener(e -> {
            loginFrame.setVisible(false);
            new RegistroUsuario(); // La conexión se maneja allá
        });

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void mostrarMenuPrincipal(Cliente cliente) {
        JFrame menuFrame = new JFrame("Panel Principal - Edutec");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridLayout(6, 1));
        menuFrame.getContentPane().setBackground(new Color(240, 240, 240));

        JLabel lblBienvenida = new JLabel("Bienvenido/a, " + cliente.getNombre(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnGestionPagos = new JButton("Gestión de Pagos");
        JButton btnClientes = new JButton("Administrar Clientes");
        JButton btnCursos = new JButton("Gestionar Cursos");
        JButton btnHistorial = new JButton("Historial de Transacciones");
        JButton btnLogout = new JButton("Cerrar Sesión");

        btnCursos.addActionListener(e -> mostrarGestionCursos(cliente));
        btnGestionPagos.addActionListener(e -> mostrarGestionPagos(cliente));
        btnClientes.addActionListener(e -> mostrarAdministrarClientes(cliente));
        btnHistorial.addActionListener(e -> mostrarHistorialTransacciones(cliente));
        btnLogout.addActionListener(e -> {
            menuFrame.dispose();
            mostrarLogin(connect()); // Reconectar
        });

        menuFrame.add(lblBienvenida);
        menuFrame.add(btnGestionPagos);
        menuFrame.add(btnClientes);
        menuFrame.add(btnCursos);
        menuFrame.add(btnHistorial);
        menuFrame.add(btnLogout);

        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    private static void mostrarGestionCursos(Cliente cliente) {
        JOptionPane.showMessageDialog(null, "Funcionalidad de Gestión de Cursos en desarrollo");
    }

    private static void mostrarGestionPagos(Cliente cliente) {
        JOptionPane.showMessageDialog(null, "Funcionalidad de Gestión de Pagos en desarrollo");
    }

    private static void mostrarAdministrarClientes(Cliente cliente) {
        JOptionPane.showMessageDialog(null, "Funcionalidad de Administrar Clientes en desarrollo");
    }

    private static void mostrarHistorialTransacciones(Cliente cliente) {
        JOptionPane.showMessageDialog(null, "Funcionalidad de Historial de Transacciones en desarrollo");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Connection conn = connect();
            if (conn != null) {
                mostrarLogin(conn);
            } else {
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
