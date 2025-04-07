package model;

public class Cliente extends Persona implements User {
    private String telefono;

    public Cliente(int id, String nombre, String correo, String telefono) {
        super(id, nombre, correo);
        this.telefono = telefono;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Cliente: " + nombre + " | Email: " + correo + " | Tel: " + telefono);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }
}