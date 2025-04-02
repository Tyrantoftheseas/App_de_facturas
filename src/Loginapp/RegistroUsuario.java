package Loginapp;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * NUEVA CLASE: Implementa la funcionalidad de registro de usuarios.
 * Permite crear nuevos usuarios en la base de datos y tiene un diseño
 * coherente con la interfaz de login original.
 */
public class RegistroUsuario extends JFrame {

    // Componentes de la interfaz
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JLabel lblMessage;

    /**
     * Constructor que crea la interfaz gráfica de registro
     */
    public RegistroUsuario() {
        setTitle("Registro de Usuario - Edutec");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        // Se usa el mismo color de fondo que en LoginApp para mantener consistencia
        getContentPane().setBackground(new Color(200, 220, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // NUEVOS COMPONENTES: Campos para el formulario de registro
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(15);

        JLabel lblApellido = new JLabel("Apellido:");
        txtApellido = new JTextField(15);

        JLabel lblCorreo = new JLabel("Correo:");
        txtCorreo = new JTextField(15);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(15);

        JLabel lblConfirmPassword = new JLabel("Confirmar Contraseña:");
        txtConfirmPassword = new JPasswordField(15);

        JButton btnRegistrar = new JButton("Registrar Usuario");
        JButton btnCancelar = new JButton("Cancelar");

        lblMessage = new JLabel("", SwingConstants.CENTER);

        // Estilo coherente con el botón de login
        btnRegistrar.setBackground(new Color(100, 150, 200));
        btnRegistrar.setForeground(Color.BLACK);  // Color de texto negro para mejor visibilidad
        btnRegistrar.setFocusPainted(false);

        // Estilo para el botón de cancelar
        btnCancelar.setBackground(new Color(200, 200, 200));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);

        // Agregar componentes al layout
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblApellido, gbc);
        gbc.gridx = 1;
        add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(lblCorreo, gbc);
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(lblConfirmPassword, gbc);
        gbc.gridx = 1;
        add(txtConfirmPassword, gbc);

        // Panel para botones con diseño fluido
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(new Color(200, 220, 240));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(panelBotones, gbc);

        gbc.gridy = 6;
        add(lblMessage, gbc);

        // EVENTOS
        btnRegistrar.addActionListener(e -> registrarUsuario());

        btnCancelar.addActionListener(e -> {
            dispose();  // Cerrar ventana de registro
            // Volver a mostrar la ventana de login
            LoginApp.mostrarLogin();
        });

        setLocationRelativeTo(null);  // Centrar la ventana
        setVisible(true);
    }

    /**
     * NUEVO MÉTODO: Valida los datos ingresados y registra al usuario en la base de datos
     */
    private void registrarUsuario() {
        // Obtener valores de los campos
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // Validaciones básicas
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios", Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarMensaje("Las contraseñas no coinciden", Color.RED);
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarMensaje("Correo electrónico inválido", Color.RED);
            return;
        }

        // Verificar si el correo ya existe
        if (usuarioExiste(correo)) {
            mostrarMensaje("El correo ya está registrado", Color.RED);
            return;
        }

        // Insertar en la base de datos
        if (insertarUsuario(nombre, apellido, correo, password)) {
            mostrarMensaje("Usuario registrado exitosamente", new Color(0, 150, 0));

            // Limpiar campos después del registro exitoso
            txtNombre.setText("");
            txtApellido.setText("");
            txtCorreo.setText("");
            txtPassword.setText("");
            txtConfirmPassword.setText("");

            // Redirigir al login después de algunos segundos
            Timer timer = new Timer(2000, e -> {
                dispose();
                LoginApp.mostrarLogin();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            mostrarMensaje("Error al registrar usuario", Color.RED);
        }
    }

    /**
     * NUEVO MÉTODO: Verifica si ya existe un usuario con el correo proporcionado
     */
    private boolean usuarioExiste(String correo) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";

        try (Connection conn = LoginApp.connect();  // Usa la conexión de LoginApp
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * NUEVO MÉTODO: Inserta un nuevo usuario en la base de datos
     */
    private boolean insertarUsuario(String nombre, String apellido, String correo, String password) {
        String query = "INSERT INTO usuarios (nombre, apellido, correo, contrasena) VALUES (?, ?, ?, ?)";

        try (Connection conn = LoginApp.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            stmt.setString(4, password);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * NUEVO MÉTODO: Muestra un mensaje en la interfaz con el color especificado
     */
    private void mostrarMensaje(String mensaje, Color color) {
        lblMessage.setText(mensaje);
        lblMessage.setForeground(color);
    }
}