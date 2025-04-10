package GUI;
import model.Cliente;
import model.Factura;
import DAO.FacturaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class HistorialFacturasGUI extends JFrame {
    private final Cliente cliente;
    private final Connection conn;
    private final FacturaDAO facturaDAO;
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;

    public HistorialFacturasGUI(Cliente cliente, Connection conn, FacturaDAO facturaDAO) {
        this.cliente = cliente;
        this.conn = conn;
        this.facturaDAO = facturaDAO;
        inicializarGUI();
        cargarHistorialFacturas();
    }

    private void inicializarGUI() {
        setTitle("Historial de Facturas - Edutec");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Fecha de Creación", "Total"}, 0);
        tablaFacturas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaFacturas);

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());
        panelBoton.add(btnVolver);

        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarHistorialFacturas() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar
        List<Factura> historialFacturas = facturaDAO.obtenerHistorialFacturasPorCliente(cliente);

        for (Factura factura : historialFacturas) {
            modeloTabla.addRow(new Object[]{
                    factura.getId(),
                    factura.getFechaCreacion(),
                    factura.getTotal()
            });
        }
    }
}