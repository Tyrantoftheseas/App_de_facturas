package Loginapp;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Clase principal para el login de usuarios.
 * Se han añadido funcionalidades para el registro de usuarios.
 */
public class LoginApp {
    // MODIFICACIÓN: Se añadió variable para la ventana de login (para poder mostrarla/ocultarla)
    private static JFrame loginFrame;

    // MODIFICACIÓN: El metodo connect() ahora es público y estático para ser usado desde RegistroUsuario
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/edutec_db";
        String user = "root";
        String password = "1234554321";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static boolean validarUsuario(String correo, String contrasena) {
        String query = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void mostrarMenuPrincipal() {
        JFrame menuFrame = new JFrame("Panel Principal - Edutec");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridLayout(4, 1));
        menuFrame.getContentPane().setBackground(new Color(240, 240, 240));

        JButton btnGestionPagos = new JButton("Gestión de Pagos");
        JButton btnClientes = new JButton("Administrar Clientes");
        JButton btnHistorial = new JButton("Historial de Transacciones");
        JButton btnLogout = new JButton("Cerrar Sesión");

        btnLogout.addActionListener(e -> {
            menuFrame.dispose();
            // MODIFICACIÓN: Ahora llama al metodo mostrarLogin() en lugar de main()
            mostrarLogin();
        });

        menuFrame.add(btnGestionPagos);
        menuFrame.add(btnClientes);
        menuFrame.add(btnHistorial);
        menuFrame.add(btnLogout);

        // MODIFICACIÓN: Se añadió esta línea para centrar la ventana
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    // MODIFICACIÓN: Se extrajo la lógica de creación de la ventana de login a un metodo separado
    // para poder mostrarla desde la clase RegistroUsuario
    public static void mostrarLogin() {
        loginFrame = new JFrame("Login Edutec");
        loginFrame.setSize(350, 300); // MODIFICACIÓN: Aumenté la altura para el nuevo botón
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

        // NUEVO: Botón para registrarse
        JButton btnRegister = new JButton("Registrarse");
        JLabel lblMessage = new JLabel("", SwingConstants.CENTER);

        btnLogin.setBackground(new Color(100, 150, 200));
        // MODIFICACIÓN: Color de texto cambiado a negro para mejor visibilidad
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);

        // NUEVO: Estilo para el botón de registro
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

        // NUEVO: Añadir botón de registrarse
        gbc.gridy = 3;
        loginFrame.add(btnRegister, gbc);

        gbc.gridy = 4;
        loginFrame.add(lblMessage, gbc);

        btnLogin.addActionListener(e -> {
            if (validarUsuario(txtUser.getText(), new String(txtPass.getPassword()))) {
                loginFrame.dispose();
                mostrarMenuPrincipal();
            } else {
                lblMessage.setText("Credenciales incorrectas");
                lblMessage.setForeground(Color.RED);
            }
        });

        // NUEVO: Evento para el botón de registro
        btnRegister.addActionListener(e -> {
            loginFrame.setVisible(false); // Ocultar temporalmente la ventana de login
            new RegistroUsuario(); // Mostrar ventana de registro
        });

        // MODIFICACIÓN: Se añadió esta línea para centrar la ventana
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    // MODIFICACIÓN: Se modificó el metodo main para usar mostrarLogin()
    public static void main(String[] args) {
        try {
            // NUEVO: Establecer el look and feel del sistema para mejor apariencia
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            mostrarLogin();
        });
    }
}