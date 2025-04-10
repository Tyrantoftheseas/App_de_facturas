package model;
import java.time.LocalDateTime;

public class Pago {
    private int id;
    private Cliente cliente; // Referencia al cliente que realizó el pago
    private double monto;
    private String metodo; // Ejemplo: "Tarjeta de Crédito", "Transferencia Bancaria", "Efectivo"
    private LocalDateTime fechaPago;
    private int facturaId; // Opcional: ID de la factura a la que se aplica el pago

    // Constructor vacío (útil para la creación desde ResultSet)
    public Pago() {
    }

    // Constructor con los atributos esenciales
    public Pago(int id, Cliente cliente, double monto, String metodo, LocalDateTime fechaPago) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
        this.metodo = metodo;
        this.fechaPago = fechaPago;
    }

    // Constructor incluyendo la factura ID (si aplica)
    public Pago(int id, Cliente cliente, double monto, String metodo, LocalDateTime fechaPago, int facturaId) {
        this(id, cliente, monto, metodo, fechaPago);
        this.facturaId = facturaId;
    }

    // Getters y setters para todos los atributos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", monto=" + monto +
                ", metodo='" + metodo + '\'' +
                ", fechaPago=" + fechaPago +
                ", facturaId=" + facturaId +
                '}';
    }
}