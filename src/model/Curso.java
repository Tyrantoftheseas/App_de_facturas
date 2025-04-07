package model;

public class Curso extends CursoBase implements Facturacion {

    public Curso(int id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public double calcularPrecio() {
        return precio; // Precio fijo
    }

    @Override
    public String getDescripcion() {
        return "Curso: " + nombre;
    }

    @Override
    public int getId() {
        return id;
    }
}