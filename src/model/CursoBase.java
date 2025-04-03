package model;

public abstract class CursoBase {
    protected int id;
    protected String nombre;
    protected double precio;

    public CursoBase(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public abstract double calcularPrecio(); // Metodo abstracto

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}


