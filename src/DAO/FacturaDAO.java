package DAO;

import model.Factura;
import model.Cliente;
import model.Curso;
import model.Facturacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private final Connection connection;

    public FacturaDAO(Connection connection) {
        this.connection = connection;
    }

    public void guardarFactura(Factura factura) {
        String queryFactura = "INSERT INTO facturas (usuario_id, fecha_emision, total, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtFactura = connection.prepareStatement(queryFactura, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtFactura.setInt(1, factura.getCliente().getId());
            stmtFactura.setTimestamp(2, Timestamp.from(Instant.now()));
            stmtFactura.setDouble(3, factura.getTotal());
            stmtFactura.setString(4, "emitida"); // Estado predeterminado

            int filasAfectadas = stmtFactura.executeUpdate();
            System.out.println("Filas afectadas al guardar factura: " + filasAfectadas);

            // Obtener el ID generado
            ResultSet generatedKeys = stmtFactura.getGeneratedKeys();
            if (generatedKeys.next()) {
                int facturaId = generatedKeys.getInt(1);
                factura.setId(facturaId); // Establecer el ID en el objeto factura
                System.out.println("Factura generada con ID: " + facturaId);
            } else {
                System.err.println("No se pudo obtener el ID de la factura generada");
            }

            // Si estás usando transacciones manuales
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la factura: " + e.getMessage());
            e.printStackTrace();
            // Hacer rollback si es necesario
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
        }
    }

    /**
     * Obtiene el historial de facturas para un cliente específico.
     * @param cliente El cliente del cual se desea obtener el historial de facturas.
     * @return Una lista de objetos Factura, o una lista vacía si no hay facturas para el cliente.
     */
    public List<Factura> obtenerHistorialFacturasPorCliente(Cliente cliente) {
        List<Factura> historialFacturas = new ArrayList<>();
        String query = "SELECT id, fecha_emision, total, estado FROM facturas WHERE usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cliente.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Factura factura = new Factura(rs.getInt("id"), cliente);
                Timestamp tsFechaEmision = rs.getTimestamp("fecha_emision");
                if (tsFechaEmision != null) {
                    factura.setFechaCreacion(tsFechaEmision.toLocalDateTime());
                }
                factura.setTotal(rs.getDouble("total"));
                // Si tu clase Factura tiene un método setEstado, úsalo aquí
                // factura.setEstado(rs.getString("estado"));
                historialFacturas.add(factura);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de facturas del cliente " + cliente.getNombre() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return historialFacturas;
    }
    public Factura obtenerFacturaPorId(int facturaId) {
        String query = "SELECT f.id, f.fecha_emision, f.total, f.estado, f.usuario_id, c.nombre, c.apellido, c.correo, c.telefono " +
                "FROM facturas f " +
                "JOIN usuarios c ON f.usuario_id = c.id " +
                "WHERE f.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("usuario_id"),
                        rs.getString("nombre") + " " + rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );

                Factura factura = new Factura(rs.getInt("id"), cliente);
                Timestamp tsFechaEmision = rs.getTimestamp("fecha_emision");
                if (tsFechaEmision != null) {
                    factura.setFechaCreacion(tsFechaEmision.toLocalDateTime());
                }
                factura.setTotal(rs.getDouble("total"));
                // Si tu clase Factura tiene un método setEstado, úsalo aquí
                // factura.setEstado(rs.getString("estado"));

                return factura;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la factura con ID " + facturaId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public List<Curso> obtenerCursosPorFactura(int idFactura) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, items.precio_unitario as precio " +
                "FROM cursos c " +
                "JOIN items_factura items ON c.id = items.curso_id " +
                "WHERE items.factura_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            ResultSet rs = stmt.executeQuery();

            // Depuración
            System.out.println("Buscando cursos para factura ID: " + idFactura);
            System.out.println("SQL ejecutado: " + sql.replace("?", String.valueOf(idFactura)));

            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                );
                System.out.println("Curso encontrado: " + curso.getDescripcion() + " - Precio: " + curso.calcularPrecio());
                cursos.add(curso);
            }

            System.out.println("Total cursos encontrados: " + cursos.size());
        } catch (SQLException e) {
            System.err.println("Error al obtener cursos de la factura: " + e.getMessage());
            e.printStackTrace();
        }

        return cursos;
    }
}