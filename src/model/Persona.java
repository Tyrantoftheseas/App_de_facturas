package model;
public abstract class Persona {
    protected int id;
    protected String nombre;
    protected String correo;

    public Persona(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public abstract void mostrarInfo();

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}



