package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HiloServidor extends Thread{
    Socket socket = null;
    DataInputStream entrada;
    HiloComun comun;

    public HiloServidor(Socket socket, HiloComun comun) throws IOException {
        this.socket = socket;
        this.comun = comun;
        // Crear el flujo para leer los mensajes
        try{
            entrada = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            System.out.println("Error leyendo los mensajes");
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void run() {
        System.out.println("Usuarios conectados: "+ comun.getActuales());
        String texto = comun.getMensajes();
        EnviarMensajes(texto);

        while(true){
            String cadena = "";
            try{
                cadena = entrada.readUTF();
                if(cadena.trim().equals("salir")){
                    comun.setActuales(comun.getActuales() -1);
                    System.out.println("Usuarios conectados: "+ comun.getActuales());
                    break;
                }
                comun.setMensajes(comun.getMensajes() + cadena + "\n");
                EnviarMensajes(comun.getMensajes());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void EnviarMensajes(String texto){
        //recorrer el chat de sockets para enviar los mensajes
        for(int i = 0; i< comun.getConexiones(); i++){
            Socket s1 = comun.getElementoChat(i);
            if(!s1.isClosed()){
                try {
                    DataOutputStream salida = new DataOutputStream(s1.getOutputStream());
                    salida.writeUTF(texto);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
