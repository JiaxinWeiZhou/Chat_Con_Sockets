package TCP;

import java.io.IOException;
import java.net.ServerSocket;

public class Servidor {
    public static void main(String[] args) {
        int puerto = 1234;
        ServerSocket servidor;
        try{
            servidor = new ServerSocket(puerto);
            System.out.println("Servidor en el puerto: "+ puerto);

            servidor.close();
        }   catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
