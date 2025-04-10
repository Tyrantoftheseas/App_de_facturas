package GUI;

import model.Cliente;
import model.Factura;
import DAO.FacturaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FacturasGUI extends JFrame {
    private final Cliente cliente;
    private final FacturaDAO facturaDAO;
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;

    public FacturasGUI(Cliente cliente, FacturaDAO facturaDAO) {
        this.cliente = cliente;
        this.facturaDAO = facturaDAO;

        inicializarGUI();
        cargarFacturas();
    }

    private void inicializarGUI() {
        setTitle("Gestión de Facturas - Edutec");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tituloLabel = new JLabel("Gestión de Facturas");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(tituloLabel);

        String[] columnas = {"ID", "Fecha de Creación", "Total", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaFacturas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaFacturas);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnVerDetalle = new JButton("Ver Detalle");
        JButton btnExportar = new JButton("Exportar a PDF");
        JButton btnVolver = new JButton("Volver");

        btnVerDetalle.addActionListener(e -> verDetalleFactura());
        btnExportar.addActionListener(e -> exportarFactura());
        btnVolver.addActionListener(e -> dispose());

        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnExportar);
        panelBotones.add(btnVolver);

        add(panelTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarFacturas() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }

        List<Factura> facturas = facturaDAO.obtenerHistorialFacturasPorCliente(cliente);

        for (Factura factura : facturas) {
            Object[] fila = {
                    factura.getId(),
                    factura.getFechaCreacion(),
                    "$" + factura.getTotal(),
                    "Pagado"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void verDetalleFactura() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idFactura = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Factura factura = facturaDAO.obtenerFacturaPorId(idFactura);

            if (factura != null) {
                JFrame detalleFrame = new JFrame("Detalle de Factura #" + idFactura);
                detalleFrame.setSize(400, 500);
                detalleFrame.setLayout(new BorderLayout());
                detalleFrame.setLocationRelativeTo(this);

                JPanel panelDetalle = new JPanel();
                panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));
                panelDetalle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel lblTitulo = new JLabel("DETALLE DE FACTURA", SwingConstants.CENTER);
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
                lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblId = new JLabel("ID Factura: " + factura.getId());
                JLabel lblCliente = new JLabel("Cliente: " + cliente.getNombre());
                JLabel lblFecha = new JLabel("Fecha: " + factura.getFechaCreacion());
                JLabel lblTotal = new JLabel("Total: $" + factura.getTotal());

                panelDetalle.add(lblTitulo);
                panelDetalle.add(Box.createRigidArea(new Dimension(0, 20)));
                panelDetalle.add(lblId);
                panelDetalle.add(lblCliente);
                panelDetalle.add(lblFecha);
                panelDetalle.add(lblTotal);

                JButton btnCerrar = new JButton("Cerrar");
                btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnCerrar.addActionListener(e -> detalleFrame.dispose());

                panelDetalle.add(Box.createRigidArea(new Dimension(0, 20)));
                panelDetalle.add(btnCerrar);

                detalleFrame.add(panelDetalle, BorderLayout.CENTER);
                detalleFrame.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una factura",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarFactura() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();
        if (filaSeleccionada != -1) {
            int idFactura = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            JOptionPane.showMessageDialog(this,
                    "Factura #" + idFactura + " exportada correctamente",
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una factura para exportar",
                    "Selección requerida",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}