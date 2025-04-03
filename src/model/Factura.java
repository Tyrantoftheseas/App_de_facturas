package model;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int id;
    private Cliente cliente;
    private List<Curso> cursos;
    private double total;


    public Factura(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.cursos = new ArrayList<>();
        this.total = 0;
    }
    public void agregarCurso(Curso curso) {
        cursos.add(curso);
        total += curso.calcularPrecio();
    }
    public void mostrarFactura() {
        System.out.println("Factura ID: " + id);
        cliente.mostrarInfo();
        System.out.println("Cursos:");
        for (Curso c : cursos) {
            System.out.println("- " + c.getNombre() + " | $" + c.getPrecio());
        }
        System.out.println("Total: $" + total);
    }
}

