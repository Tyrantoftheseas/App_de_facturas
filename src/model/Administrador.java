package model;
public class Administrador extends Persona implements Usuario {
    private String rol;

    public Administrador(int id, String nombre, String correo) {
        super(id, nombre, correo);
        this.rol = "administrador";  // Role de administrador
    }

    @Override
    public void mostrarInfo() {
        System.out.println("Administrador: " + nombre + " | Email: " + correo);
    }

    @Override
    public String getRol() {
        return rol;  // El rol de un administrador será siempre "administrador"
    }
}
