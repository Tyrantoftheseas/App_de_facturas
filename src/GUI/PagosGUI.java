package GUI;

import model.Cliente;
import model.Pago;
import DAO.PagoDAO;
import Loginapp.GestionPagos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class PagosGUI extends JFrame {
    private final Cliente cliente;
    private final PagoDAO pagoDAO;
    private final GestionPagos gestionPagos;

    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;

    public PagosGUI(Cliente cliente, Connection connection, PagoDAO pagoDAO) {
        this.cliente = cliente;
        this.pagoDAO = pagoDAO;
        this.gestionPagos = new GestionPagos(cliente, connection, pagoDAO);

        inicializarGUI();
        cargarPagos();
    }

    private void inicializarGUI() {
        setTitle("Gestión de Pagos - Edutec");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tituloLabel = new JLabel("Gestión de Pagos");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(tituloLabel);

        String[] columnas = {"ID", "Fecha", "Monto", "Método de Pago"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPagos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPagos);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNuevoPago = new JButton("Nuevo Pago");
        JButton btnDetalle = new JButton("Ver Detalle");
        JButton btnVolver = new JButton("Volver");

        btnNuevoPago.addActionListener(e -> mostrarVentanaNuevoPago());
        btnVolver.addActionListener(e -> dispose());

        panelBotones.add(btnNuevoPago);
        panelBotones.add(btnDetalle);
        panelBotones.add(btnVolver);

        add(panelTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarPagos() {
        modeloTabla.setRowCount(0);
        List<Pago> pagos = gestionPagos.obtenerPagosPorCliente(cliente);
        for (Pago pago : pagos) {
            Object[] fila = {
                    pago.getId(),
                    pago.getFechaPago(),
                    "$" + pago.getMonto(),
                    pago.getMetodo()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarVentanaNuevoPago() {
        JFrame nuevoPagoFrame = new JFrame("Nuevo Pago");
        nuevoPagoFrame.setSize(400, 250);
        nuevoPagoFrame.setLayout(new BorderLayout());
        nuevoPagoFrame.setLocationRelativeTo(this);

        JPanel panelPago = new JPanel();
        panelPago.setLayout(new BoxLayout(panelPago, BoxLayout.Y_AXIS));
        panelPago.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("REALIZAR NUEVO PAGO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMonto = new JLabel("Monto:");
        JTextField txtMonto = new JTextField(10);

        JLabel lblMetodo = new JLabel("Método de Pago:");
        String[] metodos = {"Tarjeta de Crédito", "Transferencia Bancaria", "PayPal"};
        JComboBox<String> comboMetodos = new JComboBox<>(metodos);

        JButton btnPagar = new JButton("Realizar Pago");
        btnPagar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPagar.addActionListener(e -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                String metodoPago = (String) comboMetodos.getSelectedItem();

                gestionPagos.guardarPago(cliente, monto, metodoPago);

                JOptionPane.showMessageDialog(nuevoPagoFrame,
                        "Pago realizado correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                nuevoPagoFrame.dispose();
                cargarPagos();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(nuevoPagoFrame,
                        "Ingrese un monto válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> nuevoPagoFrame.dispose());

        panelPago.add(lblTitulo);
        panelPago.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelMonto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMonto.add(lblMonto);
        panelMonto.add(txtMonto);
        panelPago.add(panelMonto);

        JPanel panelMetodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMetodo.add(lblMetodo);
        panelMetodo.add(comboMetodos);
        panelPago.add(panelMetodo);

        panelPago.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPago.add(btnPagar);
        panelPago.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPago.add(btnCancelar);

        nuevoPagoFrame.add(panelPago, BorderLayout.CENTER);
        nuevoPagoFrame.setVisible(true);
    }


}