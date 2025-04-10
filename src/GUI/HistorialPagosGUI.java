package GUI;
import model.Cliente;
import model.Pago;
import DAO.PagoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class HistorialPagosGUI extends JFrame {
    private final Cliente cliente;
    private final Connection conn;
    private final PagoDAO pagoDAO;
    private JTable tablaPagos;
    private DefaultTableModel modeloTabla;

    public HistorialPagosGUI(Cliente cliente, Connection conn, PagoDAO pagoDAO) {
        this.cliente = cliente;
        this.conn = conn;
        this.pagoDAO = pagoDAO;
        inicializarGUI();
        cargarHistorialPagos();
    }

    private void inicializarGUI() {
        setTitle("Historial de Pagos - Edutec");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Fecha", "Monto", "Método", "Factura ID"}, 0);
        tablaPagos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPagos);

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());
        panelBoton.add(btnVolver);

        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarHistorialPagos() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar
        List<Pago> historialPagos = pagoDAO.obtenerHistorialPagosPorCliente(cliente); // Corrección aquí

        for (Pago pago : historialPagos) {
            modeloTabla.addRow(new Object[]{
                    pago.getId(),
                    pago.getFechaPago(),
                    pago.getMonto(),
                    pago.getMetodo(),
                    pago.getFacturaId()
            });
        }
    }
}