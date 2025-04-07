package model;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int id;
    private Cliente cliente;
    private List<Facturacion> items;
    private double total;

    public Factura(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public void agregarItem(Facturacion item) {
        items.add(item);
        total += item.calcularPrecio();
    }

    public void mostrarFactura() {
        System.out.println("Factura ID: " + id);
        cliente.mostrarInfo();
        System.out.println("Items facturados:");
        for (Facturacion item : items) {
            System.out.println("- " + item.getDescripcion() + " | $" + item.calcularPrecio());
        }
        System.out.println("Total: $" + total);
    }

    public int getId() {
        return id;
    }

}
