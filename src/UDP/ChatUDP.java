package UDP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serial;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChatUDP extends JFrame implements ActionListener, Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    static MulticastSocket ms = null;
    static InetAddress grupo = null;
    private static final int puerto = 1111;
    byte[] buffer = new byte[1024];
    private final JTextArea textAreaTexto;
    private final JTextField mensaje;
    private final JButton btnEnviar;
    private final JButton btnSalir;
    String nombre;
    boolean online = true;

    public ChatUDP(String nombre){
        super(" VENTANA DE CHAT UDP - Usuario: " + nombre);
        this.nombre = nombre;

        JPanel jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        mensaje = new JTextField(20);
        textAreaTexto = new JTextArea(10, 40);
        textAreaTexto.setEditable(false);
        JScrollPane sp = new JScrollPane(textAreaTexto);
        btnEnviar = new JButton("Enviar");
        btnSalir = new JButton("Salir");

        //añadir en el panel
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

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        jpanel.add(btnSalir, gbc);

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
            String texto = nombre + ": "+ mensaje.getText();
            try {
                DatagramPacket paquete = new DatagramPacket(texto.getBytes(),texto.length(), grupo, puerto);
                ms.send(paquete);
                mensaje.setText("");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (e.getSource() == btnSalir) { // al salir
            String texto = nombre + " ha salido del chat";
            try {
                DatagramPacket paquete = new DatagramPacket(texto.getBytes(),texto.length(), grupo, puerto);
                ms.send(paquete);
                ms.close();
                online = false;
                System.out.println("Abandona el chat: "+ nombre);
                System.exit(0);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

    @Override
    public void run() {
        while(online){
            try {
                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                ms.receive(p);
                String texto = new String(p.getData(), 0, p.getLength());
                textAreaTexto.append(texto + "\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args) throws IOException {
        System.out.println("Servidor en puerto "+ puerto);
        ms = new MulticastSocket(puerto);
        grupo = InetAddress.getByName("225.0.0.1");
        ms.joinGroup(grupo);

        String nombre;
        do{
            nombre = JOptionPane.showInputDialog("Introduce tu nombre:");
            if (nombre == null) {
                System.exit(0);
            }
            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre está vacío. Inténtalo de nuevo.");
            }else{
                break;
            }
        }while(true);

        ChatUDP server = new ChatUDP(nombre);
        server.setVisible(true);
        new Thread(server).start();
    }
}
