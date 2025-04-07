package App;

import model.Correo;

public class Main {
    public static void main(String[] args) {
        Correo correo = new Correo("cliente@example.com", "Bienvenido", "Gracias por registrarte en nuestro sistema.");

        correo.enviarNotificacion(correo);
        correo.enviarNotificacion("otro@example.com", "Recordatorio", "No olvides tu curso el lunes.");
        }
    }