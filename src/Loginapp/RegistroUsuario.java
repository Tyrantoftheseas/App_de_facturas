package Loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

import static Loginapp.LoginApp.connect;

public class RegistroUsuario extends JFrame {

    public RegistroUsuario(Connection conn) {
        setTitle("Registro de Usuario");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(220, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos del formulario
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(15);

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField(15);

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField txtCorreo = new JTextField(15);

        JLabel lblContrasena = new JLabel("Contraseña:");
        JPasswordField txtContrasena = new JPasswordField(15);

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField txtTelefono = new JTextField(15);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        // Estilizar botones
        btnRegistrar.setBackground(new Color(100, 180, 120));
        btnRegistrar.setForeground(Color.BLACK);

        btnCancelar.setBackground(new Color(180, 100, 100));
        btnCancelar.setForeground(Color.BLACK);

        gbc.gridx = 0; gbc.gridy = 0;
        add(lblNombre, gbc); gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(lblApellido, gbc); gbc.gridx = 1;
        add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(lblCorreo, gbc); gbc.gridx = 1;
        add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(lblContrasena, gbc); gbc.gridx = 1;
        add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(lblTelefono, gbc); gbc.gridx = 1;
        add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnRegistrar, gbc);

        gbc.gridy++;
        add(btnCancelar, gbc);

        // Acción del botón Registrar
        btnRegistrar.addActionListener((ActionEvent e) -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContrasena.getPassword());
            String telefono = txtTelefono.getText().trim();

            // Validaciones básicas
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.");
                return;
            }

            // Validar si el correo ya existe
            if (correoExiste(correo)) {
                JOptionPane.showMessageDialog(this, "El correo electrónico ya está registrado.");
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
                        stmt.executeUpdate();
                    }

                    // Notificación de registro
                    String getUserId = "SELECT id FROM usuarios WHERE correo = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(getUserId)) {
                        stmt.setString(1, correo);
                        var rs = stmt.executeQuery();
                        if (rs.next()) {
                            int id = rs.getInt("id");
                        }
                    }

                    JOptionPane.showMessageDialog(this, "Registro exitoso. Puede iniciar sesión.");
                    dispose();
                    LoginApp.mostrarLogin(connect()); // Volver a login
                } else {
                    JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario: " + ex.getMessage());
            }
        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> {
            dispose();
            LoginApp.mostrarLogin(connect());
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para verificar si el correo ya está registrado
    private boolean correoExiste(String correo) {
        String query = "SELECT id FROM usuarios WHERE correo = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, correo);

            try (var rs = stmt.executeQuery()) {
                return rs.next();  // Si ya existe un correo, retornará true
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de correo: " + e.getMessage());
        }

        return false;  // Retorna false si no se encuentra el correo
    }
}