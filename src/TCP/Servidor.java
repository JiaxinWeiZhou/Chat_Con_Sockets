package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int puerto = 1234;
        ServerSocket servidor;
        int maximo = 10;
        Socket chat [] = new Socket[maximo];
        HiloComun comun = new HiloComun(maximo, 0, 0, chat);

        servidor = new ServerSocket(puerto);
        System.out.println("Servidor en el puerto: "+ puerto);

        while(comun.getConexiones() < maximo){
            Socket socket = new Socket();
            socket = servidor.accept(); // espera al usuario

            comun.addChat(socket, comun.getConexiones());
            comun.setActuales(comun.getActuales()+1);
            comun.setConexiones(comun.getConexiones()+1);

            HiloServidor hilo = new HiloServidor(socket, comun);
            hilo.start();
        }
        servidor.close();

    }
}
