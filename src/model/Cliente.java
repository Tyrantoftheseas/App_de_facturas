package model;
public class Cliente extends Persona implements Usuario {
    private String telefono;
    private String rol;  // Rol del usuario, puede ser "cliente" o algo más

    public Cliente(int id, String nombre, String correo, String telefono) {
        super(id, nombre, correo);
        this.telefono = telefono;
        this.rol = "cliente";  // Role por defecto
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Cliente: " + nombre + " | Email: " + correo + " | Tel: " + telefono);
    }

    @Override
    public String getRol() {
        return rol;  // El rol de un cliente será siempre "cliente"
    }

    public String getTelefono() {
        return telefono;
    }
}
