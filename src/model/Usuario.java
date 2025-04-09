package model;
public interface Usuario {
        int getId();            // Obtiene el ID del usuario
        String getNombre();     // Obtiene el nombre del usuario
        String getCorreo();     // Obtiene el correo del usuario
        void mostrarInfo();     // Muestra la información del usuario
        String getRol();        // Obtiene el rol del usuario (por ejemplo: "administrador", "cliente", etc.)
}
