package TCP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteChat extends JFrame implements ActionListener, Runnable{
    private static final long serialVersionUID = 1L;
    Socket socket = null;
    DataInputStream entrada;
    DataOutputStream salida;
    String nombre;
    private JTextField mensaje;
    private JTextArea textArea1;
    private JButton btnEnviar;
    private JButton btnSalir;
    private JPanel jpanel;
    boolean repetir = true;
    boolean primerMensaje = true;


    public ClienteChat(Socket socket, String nombre){
        super("Usuario: " + nombre+ " conectado");
        this.socket = socket;
        this.nombre = nombre;

        jpanel = new JPanel();
        mensaje = new JTextField(40);
        textArea1 = new JTextArea(15, 40);
        btnEnviar = new JButton("Enviar");
        btnSalir = new JButton("Salir");

        //añadir en el panel
        jpanel.add(mensaje);
        jpanel.add(btnEnviar);
        JScrollPane sp = new JScrollPane(textArea1);
        jpanel.add(sp);
        jpanel.add(btnSalir);

        btnSalir.addActionListener(this);
        btnEnviar.addActionListener(this);
        try {
            entrada = new DataInputStream(socket.getInputStream());
            salida = new DataOutputStream(socket.getOutputStream());
            String texto = "Usuario:" + nombre + " esta en linea";

            salida.writeUTF(texto);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        add(jpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEnviar) { // al enviar
            if (mensaje.getText().trim().length() == 0){
                return;
            }
            String texto = nombre + ": " + mensaje.getText();
            try {
                mensaje.setText("");
                salida.writeUTF(texto);
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
        if (e.getSource() == btnSalir) { // al salir
            String texto = " > Abandona el Chat ... " + nombre;
            try {
                salida.writeUTF(texto);
                salida.writeUTF("salir");
                repetir = false;
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    @Override
    public void run() {
        String texto = "";
        while (repetir) {
            try {
                texto = entrada.readUTF();
                if (primerMensaje) { // Solo muestra el primer mensaje
                    textArea1.append(texto + "\n");
                    primerMensaje = false;
                } else if (!texto.isEmpty()) { // Muestra los mensajes nuevos
                    textArea1.append(texto + "\n");
                }
            } catch (IOException e) {
                System.out.println("Se ha cerrado el servidor");
                repetir = false;
            }
        }
        try {
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        int puerto = 1234;
        Socket socket = null;

        String nombre = JOptionPane.showInputDialog("Introduce tu nombre o nick:");
        if (nombre.trim().length() == 0) {
            System.out.println("El nombre está vacío....");
            return;
        }
        try {
            socket = new Socket("localhost", puerto);
            ClienteChat cliente = new ClienteChat(socket, nombre);
            new Thread(cliente).start();

        } catch (IOException e) {
            System.out.println("Error al intentar conectar con el servidor");
        }
    }
}
