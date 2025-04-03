package model;

public class Cliente extends Persona {
    private String telefono;

    public Cliente(int id, String nombre, String correo, String telefono) {
        super(id, nombre, correo);
        this.telefono = telefono;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Cliente: " + nombre + " | Email: " + correo + " | Tel: " + telefono);

    }
    public String getTelefono() { return telefono; }

}



