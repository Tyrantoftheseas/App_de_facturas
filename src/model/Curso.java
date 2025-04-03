package model;
public class Curso extends CursoBase {
    public Curso(int id, String nombre, double precio) {
        super(id, nombre, precio);
    }

    @Override
    public double calcularPrecio() {
        return precio; // Precio fijo
    }
}

