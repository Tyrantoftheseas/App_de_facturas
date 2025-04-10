package Loginapp;
import model.Cliente;
import model.Correo;
import model.Curso;
import model.Factura;
import DAO.FacturaDAO;

import javax.swing.*;
import java.awt.*;

public class GestionFacturas {
    private final FacturaDAO facturaDAO;

    public GestionFacturas(FacturaDAO facturaDAO) {
        this.facturaDAO = facturaDAO;
    }

    // Método para generar una factura
    public void generarFactura(Cliente cliente, Curso item) {
        JFrame facturaFrame = new JFrame("Factura - Edutec");
        facturaFrame.setSize(400, 500);
        facturaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        facturaFrame.setLayout(new BorderLayout());

// En tu clase de gestión de facturas (ej., GestionFacturas.java)
        Factura factura = new Factura(0, cliente);
        factura.agregarItem(item); // Agrega el ítem a la factura

// Guardar la factura completa (con sus ítems)
        facturaDAO.guardarFactura(factura);

        // Panel para mostrar la información de la factura
        JPanel panelFactura = new JPanel();
        panelFactura.setLayout(new BoxLayout(panelFactura, BoxLayout.Y_AXIS));
        panelFactura.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("FACTURA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblId = new JLabel("ID Factura: " + factura.getId());
        JLabel lblCliente = new JLabel("Cliente: " + cliente.getNombre());
        JLabel lblEmail = new JLabel("Email: " + cliente.getCorreo());
        JLabel lblTelefono = new JLabel("Teléfono: " + cliente.getTelefono());

        JLabel lblItems = new JLabel("ITEMS FACTURADOS:");
        lblItems.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panelItems = new JPanel(new GridLayout(0, 1));
        panelItems.setBorder(BorderFactory.createEtchedBorder());

        JLabel lblDescripcion = new JLabel(item.getDescripcion());
        JLabel lblPrecio = new JLabel("Precio: $" + item.calcularPrecio());

        panelItems.add(lblDescripcion);
        panelItems.add(lblPrecio);

        JLabel lblTotal = new JLabel("TOTAL: $" + item.calcularPrecio());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));

        // Añadir botón para enviar por correo
        JButton btnEnviarCorreo = new JButton("Enviar factura por correo");
        btnEnviarCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEnviarCorreo.addActionListener(e -> {
            // Usar la clase Correo para enviar la factura por email
            Correo mailFactura = new Correo(
                    cliente.getCorreo(),
                    "Factura Edutec #" + factura.getId(),
                    "Estimado/a " + cliente.getNombre() + ",\n\n" +
                            "Adjuntamos su factura por la compra de: " + item.getDescripcion() + "\n" +
                            "Monto: $" + item.calcularPrecio() + "\n\n" +
                            "Gracias por confiar en Edutec.\n" +
                            "Este es un correo automático, por favor no responda a este mensaje."
            );

            mailFactura.enviarNotificacion(mailFactura);
            JOptionPane.showMessageDialog(facturaFrame,
                    "Factura enviada por correo a: " + cliente.getCorreo(),
                    "Correo enviado",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> facturaFrame.dispose());

        panelFactura.add(lblTitulo);
        panelFactura.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFactura.add(lblId);
        panelFactura.add(lblCliente);
        panelFactura.add(lblEmail);
        panelFactura.add(lblTelefono);
        panelFactura.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFactura.add(lblItems);
        panelFactura.add(panelItems);
        panelFactura.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFactura.add(lblTotal);
        panelFactura.add(Box.createRigidArea(new Dimension(0, 30)));
        panelFactura.add(btnEnviarCorreo);
        panelFactura.add(Box.createRigidArea(new Dimension(0, 10)));
        panelFactura.add(btnCerrar);

        facturaFrame.add(panelFactura, BorderLayout.CENTER);
        facturaFrame.setLocationRelativeTo(null);
        facturaFrame.setVisible(true);

        // Ya no necesitas la simulación aquí, el DAO se encarga
        System.out.println("Factura generada y (intentando) guardada: #" + factura.getId() + " para " + cliente.getNombre());
    }

    // El método para generar ID ahora estará en el DAO
    // private static int generateFacturaId() { ... }
}
