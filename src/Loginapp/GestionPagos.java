package Loginapp;

import DAO.PagoDAO;
import DAO.FacturaDAO;
import model.Cliente;
import model.Pago;
import model.Factura;

import java.sql.Connection;
import java.util.List;

public class GestionPagos {
    private final PagoDAO pagoDAO;
    private final FacturaDAO facturaDAO;
    private final Cliente cliente;
    private final Connection conn;

    public GestionPagos(Cliente cliente, Connection conn, PagoDAO pagoDAO) {
        this.cliente = cliente;
        this.conn = conn;
        this.pagoDAO = pagoDAO;
        this.facturaDAO = new FacturaDAO(conn); // Inicializar FacturaDAO
    }

    // Registrar un pago simple con factura automática
    public void registrarPago(double monto, String metodo) {
        // Crear y guardar la factura primero
        Factura factura = new Factura(0, cliente);
        factura.setTotal(monto);

        // Guardar factura en la base de datos
        facturaDAO.guardarFactura(factura);

        // Verificar si la factura se guardó correctamente
        if (factura.getId() > 0) {
            // Ahora registrar el pago con el ID de la factura
            Pago pago = new Pago();
            pago.setCliente(cliente);
            pago.setMonto(monto);
            pago.setMetodo(metodo);
            pago.setFacturaId(factura.getId());

            pagoDAO.registrarPagoConFactura(pago);
            System.out.println("Pago registrado exitosamente con factura ID: " + factura.getId());
        } else {
            System.err.println("No se pudo crear la factura, el pago no se registrará");
        }
    }

    // Registrar un pago con una factura asociada existente
    public void registrarPagoConFactura(Pago pago) {
        pagoDAO.registrarPagoConFactura(pago);
    }

    // Obtener historial de pagos del cliente
    public List<Pago> obtenerHistorialPagos() {
        return pagoDAO.obtenerHistorialPagosPorCliente(cliente);
    }

    // Método usado por PagosGUI para obtener los pagos
    public List<Pago> obtenerPagosPorCliente(Cliente cliente) {
        return pagoDAO.obtenerHistorialPagosPorCliente(cliente);
    }

    // Método usado por PagosGUI para guardar un nuevo pago
    public void guardarPago(Cliente cliente, double monto, String metodoPago) {
        // Este método ahora generará una factura automáticamente
        registrarPago(monto, metodoPago);
    }
}
