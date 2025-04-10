package DAO;

import model.Cliente;
import model.Pago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    private final Connection connection;

    public PagoDAO(Connection connection) {
        this.connection = connection;
    }

    public void registrarPago(Cliente cliente, double monto, String metodo) {
        String query = "INSERT INTO pagos (usuario_id, monto, metodo_pago, fecha_pago) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cliente.getId());
            stmt.setDouble(2, monto);
            stmt.setString(3, metodo);
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.executeUpdate();

            // Agregar commit si es necesario
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el pago: " + e.getMessage());
            e.printStackTrace();
            // Hacer rollback si es necesario
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
        }
    }
    public void registrarPagoConFactura(Pago pago) {
        String query = "INSERT INTO pagos (usuario_id, factura_id, monto, fecha_pago, metodo_pago) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, pago.getCliente().getId());
            stmt.setInt(2, pago.getFacturaId());
            stmt.setDouble(3, pago.getMonto());
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.setString(5, pago.getMetodo());
            stmt.executeUpdate();

            // Agregar commit si es necesario
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el pago con factura: " + e.getMessage());
            e.printStackTrace();
            // Hacer rollback si es necesario
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
        }
    }
    /**
     * Obtiene el historial de pagos para un cliente específico.
     * @param cliente El cliente del cual se desea obtener el historial de pagos.
     * @return Una lista de objetos Pago, o una lista vacía si no hay pagos para el cliente.
     */
    public List<Pago> obtenerHistorialPagosPorCliente(Cliente cliente) {
        List<Pago> historialPagos = new ArrayList<>();
        String query = "SELECT id, monto, metodo_pago, fecha_pago, factura_id FROM pagos WHERE usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cliente.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pago pago = new Pago();
                pago.setId(rs.getInt("id"));
                pago.setCliente(cliente); // Asocia el cliente al pago
                pago.setMonto(rs.getDouble("monto"));
                pago.setMetodo(rs.getString("metodo_pago")); // Cambio de "metodo" a "metodo_pago"
                Timestamp tsFechaPago = rs.getTimestamp("fecha_pago");
                if (tsFechaPago != null) {
                    pago.setFechaPago(tsFechaPago.toLocalDateTime());
                }
                pago.setFacturaId(rs.getInt("factura_id"));
                historialPagos.add(pago);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de pagos del cliente " + cliente.getNombre() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return historialPagos;
    }
}