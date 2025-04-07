package Loginapp;
import model.Cliente;
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

    private static Cliente validarUsuario(String correo, String contrasena) {
        String query = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Crear objeto Cliente con los datos del usuario
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre") + " " + rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono") != null ? rs.getString("telefono") : "No disponible"
                );

                // Notificar login exitoso
                notificarLoginExitoso(correo);

                return cliente;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void notificarLoginExitoso(String correo) {
        model.Correo notificacion = new model.Correo(
                correo,
                "Inicio de sesión exitoso - Edutec",
                "Se ha detectado un inicio de sesión en tu cuenta Edutec. " +
                        "Si no fuiste tú, por favor contacta a soporte técnico."
        );
        notificacion.enviarNotificacion(notificacion);
    }

    // Modificar el método mostrarMenuPrincipal para recibir el cliente
    private static void mostrarMenuPrincipal(Cliente cliente) {
        JFrame menuFrame = new JFrame("Panel Principal - Edutec");
        menuFrame.setSize(400, 300);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLayout(new GridLayout(6, 1)); // Ajustado a 6 filas para todos los componentes
        menuFrame.getContentPane().setBackground(new Color(240, 240, 240));

        // Añadir etiqueta de bienvenida con el nombre del cliente
        JLabel lblBienvenida = new JLabel("Bienvenido/a, " + cliente.getNombre(), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnGestionPagos = new JButton("Gestión de Pagos");
        JButton btnClientes = new JButton("Administrar Clientes");
        JButton btnCursos = new JButton("Gestionar Cursos"); // Nuevo botón
        JButton btnHistorial = new JButton("Historial de Transacciones");
        JButton btnLogout = new JButton("Cerrar Sesión");

        // Añadir ActionListener para el botón de Cursos
        btnCursos.addActionListener(e -> mostrarGestionCursos(cliente));

        // Añadir ActionListener para el botón de Gestión de Pagos
        btnGestionPagos.addActionListener(e -> mostrarGestionPagos(cliente));

        // Añadir ActionListener para el botón de Administrar Clientes
        btnClientes.addActionListener(e -> mostrarAdministrarClientes(cliente));

        // Añadir ActionListener para el botón de Historial
        btnHistorial.addActionListener(e -> mostrarHistorialTransacciones(cliente));

        btnLogout.addActionListener(e -> {
            menuFrame.dispose();
            mostrarLogin();
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

    // Métodos para manejar las diferentes secciones (implementar según se necesite)
    private static void mostrarGestionCursos(Cliente cliente) {
        // TODO: Implementar la gestión de cursos
        JOptionPane.showMessageDialog(null, "Funcionalidad de Gestión de Cursos en desarrollo");
    }

    private static void mostrarGestionPagos(Cliente cliente) {
        // TODO: Implementar la gestión de pagos
        JOptionPane.showMessageDialog(null, "Funcionalidad de Gestión de Pagos en desarrollo");
    }

    private static void mostrarAdministrarClientes(Cliente cliente) {
        // TODO: Implementar la administración de clientes
        JOptionPane.showMessageDialog(null, "Funcionalidad de Administrar Clientes en desarrollo");
    }

    private static void mostrarHistorialTransacciones(Cliente cliente) {
        // TODO: Implementar el historial de transacciones
        JOptionPane.showMessageDialog(null, "Funcionalidad de Historial de Transacciones en desarrollo");
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
            Cliente cliente = validarUsuario(txtUser.getText(), new String(txtPass.getPassword()));
            if (cliente != null) {
                loginFrame.dispose();
                mostrarMenuPrincipal(cliente);
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