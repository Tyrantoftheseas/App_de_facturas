package Loginapp;
import model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GestionPagos {
    // Metodo para mostrar la gestión de pagos
    public static void mostrarGestionPagos(Cliente cliente) {
        JFrame pagosFrame = new JFrame("Gestión de Pagos - Edutec");
        pagosFrame.setSize(500, 400);
        pagosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pagosFrame.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        JLabel lblCliente = new JLabel("Cliente: " + cliente.getNombre(), SwingConstants.CENTER);
        lblCliente.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblInstrucciones = new JLabel("Seleccione un método de pago:", SwingConstants.CENTER);

        panelInfo.add(lblCliente);
        panelInfo.add(lblInstrucciones);

        JPanel panelOpciones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnTarjeta = new JButton("Pago con Tarjeta");
        JButton btnTransferencia = new JButton("Transferencia Bancaria");
        JButton btnVolver = new JButton("Volver");

        panelOpciones.add(btnTarjeta);
        panelOpciones.add(btnTransferencia);
        panelOpciones.add(btnVolver);

        btnVolver.addActionListener(e -> pagosFrame.dispose());

        pagosFrame.add(panelInfo, BorderLayout.NORTH);
        pagosFrame.add(panelOpciones, BorderLayout.CENTER);
        pagosFrame.setLocationRelativeTo(null);
        pagosFrame.setVisible(true);
    }
}