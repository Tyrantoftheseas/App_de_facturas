package Loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class RegistroUsuario extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmContrasena;
    private JTextField txtTelefono;
    private JLabel lblMessage;
    private Connection conn;

    public RegistroUsuario(Connection conn) {
        this.conn = conn;

        // Configuración básica de la ventana
        setTitle("Registro de Usuario - Edutec");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(180, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(8, 8, 8, 8);
        pgbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 150, 243));

        // Campos del formulario
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtNombre = new JTextField(15);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtApellido = new JTextField(15);
        txtApellido.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtCorreo = new JTextField(15);
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblConfirmContrasena = new JLabel("Confirmar Contraseña:");
        lblConfirmContrasena.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtConfirmContrasena = new JPasswordField(15);
        txtConfirmContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Times New Roman", Font.BOLD, 14));
        txtTelefono = new JTextField(15);
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Botones
        JButton btnRegistrar = new JButton("Registrar Usuario");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilo de botones
        btnRegistrar.setBackground(new Color(0, 123, 255));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Mensaje de estado
        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setPreferredSize(new Dimension(350, 30));

        // Añadir componentes al panel
        pgbc.gridx = 0; pgbc.gridy = 0; pgbc.gridwidth = 2;
        panel.add(lblTitulo, pgbc);

        pgbc.gridwidth = 1;
        pgbc.gridx = 0; pgbc.gridy = 1;
        panel.add(lblNombre, pgbc);
        pgbc.gridx = 1;
        panel.add(txtNombre, pgbc);

        pgbc.gridx = 0; pgbc.gridy = 2;
        panel.add(lblApellido, pgbc);
        pgbc.gridx = 1;
        panel.add(txtApellido, pgbc);

        pgbc.gridx = 0; pgbc.gridy = 3;
        panel.add(lblCorreo, pgbc);
        pgbc.gridx = 1;
        panel.add(txtCorreo, pgbc);

        pgbc.gridx = 0; pgbc.gridy = 4;
        panel.add(lblContrasena, pgbc);
        pgbc.gridx = 1;
        panel.add(txtContrasena, pgbc);

        pgbc.gridx = 0; pgbc.gridy = 5;
        panel.add(lblConfirmContrasena, pgbc);
        pgbc.gridx = 1;
        panel.add(txtConfirmContrasena, pgbc);

        pgbc.gridx = 0; pgbc.gridy = 6;
        panel.add(lblTelefono, pgbc);
        pgbc.gridx = 1;
        panel.add(txtTelefono, pgbc);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        pgbc.gridx = 0; pgbc.gridy = 7; pgbc.gridwidth = 2;
        panel.add(panelBotones, pgbc);

        pgbc.gridy = 8;
        panel.add(lblMessage, pgbc);

        // Añadir panel principal
        gbc.gridx = 0; gbc.gridy = 0;
        add(panel, gbc);

        // Acciones de botones
        btnRegistrar.addActionListener((ActionEvent e) -> registrarUsuario());

        btnCancelar.addActionListener(e -> {
            dispose();
            LoginApp.mostrarLogin(conn);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String confirmContrasena = new String(txtConfirmContrasena.getPassword());
        String telefono = txtTelefono.getText().trim();

        // Validaciones básicas
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Todos los campos obligatorios deben ser completados.", Color.RED);
            return;
        }

        if (!contrasena.equals(confirmContrasena)) {
            mostrarMensaje("Las contraseñas no coinciden.", Color.RED);
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarMensaje("El formato del correo electrónico es inválido.", Color.RED);
            return;
        }

        // Validar si el correo ya existe
        if (correoExiste(correo)) {
            mostrarMensaje("El correo electrónico ya está registrado.", Color.RED);
            return;
        }

        try {
            if (conn != null) {
                String insert = "INSERT INTO usuarios (nombre, apellido, correo, contrasena, telefono, ultimo_acceso) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, apellido);
                    stmt.setString(3, correo);
                    stmt.setString(4, contrasena);
                    stmt.setString(5, telefono);
                    stmt.setTimestamp(6, java.sql.Timestamp.from(Instant.now()));

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarMensaje("Registro exitoso. Puede iniciar sesión.", new Color(0, 150, 0));

                        // Limpiar campos
                        txtNombre.setText("");
                        txtApellido.setText("");
                        txtCorreo.setText("");
                        txtContrasena.setText("");
                        txtConfirmContrasena.setText("");
                        txtTelefono.setText("");

                        // Timer para volver al login
                        Timer timer = new Timer(2000, e -> {
                            dispose();
                            LoginApp.mostrarLogin(conn);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        mostrarMensaje("Error al registrar el usuario.", Color.RED);
                    }
                }
            } else {
                mostrarMensaje("Error de conexión a la base de datos.", Color.RED);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensaje("Error al registrar: " + ex.getMessage(), Color.RED);
        }
    }

    // Método para verificar si el correo ya está registrado
    private boolean correoExiste(String correo) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de correo: " + e.getMessage());
        }

        return false;
    }

    private void mostrarMensaje(String mensaje, Color color) {
        lblMessage.setText(mensaje);
        lblMessage.setForeground(color);
    }
}