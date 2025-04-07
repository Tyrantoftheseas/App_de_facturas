package model;
public class Correo implements EmailNotificador {
    private String destinatario;
    private String asunto;
    private String mensaje;

    public Correo(String destinatario, String asunto, String mensaje) {
        if (destinatario == null || destinatario.trim().isEmpty()) {
            throw new IllegalArgumentException("El destinatario no puede estar vacio.");
        }

        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    public String getDestinatario() { return destinatario; }
    public String getAsunto() { return asunto; }
    public String getMensaje() { return mensaje; }

    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) {
        System.out.println("Enviando correo a: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
    }

    @Override
    public void enviarNotificacion(Correo correo) {
        enviarNotificacion(correo.getDestinatario(), correo.getAsunto(), correo.getMensaje());
    }
}