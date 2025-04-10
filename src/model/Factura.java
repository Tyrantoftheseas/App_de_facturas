package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class Factura {
    private int id;
    private Cliente cliente;
    private List<Facturacion> items;
    private double total;
    private LocalDateTime fechaCreacion; // Muevo la declaración aquí

    public Factura(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.total = 0;
        this.fechaCreacion = LocalDateTime.now(); // Inicializo la fecha de creación al crear la factura
    }

    public void agregarItem(Facturacion item) {
        items.add(item);
        total += item.calcularPrecio();
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void mostrarFactura() {
        System.out.println("Factura ID: " + id);
        cliente.mostrarInfo();
        System.out.println("Fecha de Creación: " + fechaCreacion);
        System.out.println("Items facturados:");
        for (Facturacion item : items) {
            System.out.println("- " + item.getDescripcion() + " | $" + item.calcularPrecio());
        }
        System.out.println("Total: $" + total);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Facturacion> getItems() {
        return items;
    }

    public void setItems(List<Facturacion> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}