package TCP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.net.Socket;

public class ClienteChat extends JFrame implements ActionListener, Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    Socket socket;
    DataInputStream entrada;
    DataOutputStream salida;
    String nombre;
    private final JTextArea textAreaTexto;
    private final JTextField mensaje;
    private final JButton btnEnviar;
    private final JButton btnSalir;
    private final JPanel jpanel;
    private final JLabel lblTitulo;
    private boolean online = true;

    public ClienteChat(Socket socket, String nombre) throws IOException {
        System.out.println("Usuario: " + nombre + " conectado");
        this.socket = socket;
        this.nombre = nombre;
        this.entrada = new DataInputStream(socket.getInputStream());
        this.salida = new DataOutputStream(socket.getOutputStream());

        salida.writeUTF(nombre);

        jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        lblTitulo = new JLabel(nombre + " conectado a server: " + socket.getPort());
        mensaje = new JTextField(20);
        textAreaTexto = new JTextArea(10, 40);
        textAreaTexto.setEditable(false);
        JScrollPane sp = new JScrollPane(textAreaTexto);
        btnEnviar = new JButton("Enviar");
        btnSalir = new JButton("Salir");

        //añadir en el panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.WEST;
        jpanel.add(lblTitulo, gbc);

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
            String texto = mensaje.getText().trim();
            if (!texto.isEmpty()) {
                String textoCompleto = nombre + ": " + texto;
                try {
                    textAreaTexto.append(textoCompleto + "\n");
                    salida.writeUTF(textoCompleto);// enviar mensaje al servidor
                    mensaje.setText(""); // limpiar el mensaje
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        if (e.getSource() == btnSalir) { // al salir
            String textoSalir = "\n" + nombre + " ha abandonado el chat";
            try {
                textAreaTexto.append(textoSalir);
                salida.writeUTF(textoSalir);
                salida.writeUTF("salir");
                online = false;
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    public void solicitarHistorial() {
        try {
            salida.writeUTF("historial"); // Enviar solicitud de historial al servidor
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            solicitarHistorial();

            while (online) {
                String mensajeRecibido = entrada.readUTF();
                if (!mensajeRecibido.startsWith(nombre + ":")) { // si el mensaje no empieza con el nombre del cliente lo muestra
                textAreaTexto.append(mensajeRecibido + "\n");
             }
            }
        } catch (IOException e) {
            System.out.println("No se ha podido conectar con el servidor");
            online = false;
        } finally {
            try {
                socket.close();
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int puerto = 1234;
        Socket socket;

        String nombre = JOptionPane.showInputDialog("Introduce tu nombre:");
        if (nombre.trim().isEmpty()) {
            System.out.println("El nombre está vacío...");
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
