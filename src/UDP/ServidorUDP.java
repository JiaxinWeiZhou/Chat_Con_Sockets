package UDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ServidorUDP extends JFrame implements ActionListener, Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static DatagramSocket socket;
    private static Set<String> usuarios = new HashSet<>();
    private static final int puerto = 1111;
    byte[] buffer = new byte[1024];
    private JTextArea textAreaTexto;
    private JTextField mensaje;
    private JButton btnEnviar;
    private JButton btnSalir;
    private JPanel jpanel;
    String nombre;

    public ServidorUDP(){

        jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        mensaje = new JTextField(20);
        textAreaTexto = new JTextArea(10, 40);
        textAreaTexto.setEditable(false);
        JScrollPane sp = new JScrollPane(textAreaTexto);
        btnEnviar = new JButton("Enviar");
        btnSalir = new JButton("Salir");

        //añadir en el panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        jpanel.add(btnSalir, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpanel.add(mensaje, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jpanel.add(btnEnviar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        jpanel.add(sp, gbc);

        btnSalir.addActionListener(this);
        btnEnviar.addActionListener(this);

        add(jpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnviar) { // al enviar
            String texto = mensaje.getText();
            //buffer = mensaje.getBytes();

            mensaje.setText("");
        }

        if (e.getSource() == btnSalir) { // al salir
            System.exit(0);
        }
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) throws SocketException {
        socket = new DatagramSocket(puerto);
        System.out.println("Servidor en puerto "+ puerto);

        ServidorUDP server = new ServidorUDP();


    }
}
