package model;

public interface EmailNotificador {
    void enviarNotificacion(String destinatario, String asunto, String mensaje);
    void enviarNotificacion(Correo correo);
}