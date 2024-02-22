package TCP;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int puerto = 1234;
        ServerSocket servidor;
        int maximo = 10;
        Socket[] chat;
        chat = new Socket[maximo];
        HiloComun comun = new HiloComun(maximo, chat);

        Set<String> nombres = new HashSet<>();

        servidor = new ServerSocket(puerto);
        System.out.println("Servidor en el puerto: "+ puerto);

        while(comun.getConexiones() < maximo){
            Socket socket = servidor.accept(); // espera al usuario

            String nombreCliente = new DataInputStream(socket.getInputStream()).readUTF();

            if (nombres.contains(nombreCliente)) {
                // si ya existe
                System.out.println("El nombre de usuario '" + nombreCliente + "' ya está en uso. Rechazando la conexión.");
                socket.close();
            } else {
                nombres.add(nombreCliente);

                comun.addChat(socket, comun.getConexiones());
                comun.setActuales(comun.getActuales() + 1);
                comun.setConexiones(comun.getConexiones() + 1);

                HiloServidor hilo = new HiloServidor(socket, comun, nombres , nombreCliente);
                hilo.start();
                System.out.println("Usuario '" + nombreCliente + "' conectado.");
            }
        }
        servidor.close();


    }
}
