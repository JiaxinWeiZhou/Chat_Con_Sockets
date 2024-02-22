package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class HiloServidor extends Thread{
    Socket socket;
    DataInputStream entrada;
    HiloComun comun;
    Set<String> nombres;
    String nombreCliente;

    public HiloServidor(Socket socket, HiloComun comun, Set<String> nombres, String nombreCliente){
        this.socket = socket;
        this.comun = comun;
        this.nombres = nombres;
        this.nombreCliente =nombreCliente;

        // Crear el flujo para leer los mensajes
        try{
            entrada = new DataInputStream(socket.getInputStream());
        }catch (IOException e){
            System.out.println("Error leyendo los mensajes");
            System.out.println(e.getMessage());
        }
    }

    public void EnviarHistorial(Socket cliente){
        try {
            DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());
            salida.writeUTF(comun.getMensajes()); // Enviar historial al cliente
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Usuarios conectados: "+ comun.getActuales());
        nombres.add(nombreCliente);
        try {
            while (true) {
                String mensajeRecibido = entrada.readUTF();
                if (mensajeRecibido.trim().equals("salir")) {
                    comun.setActuales(comun.getActuales() - 1);
                    System.out.println("Usuarios conectados: "+ comun.getActuales());
                    for(int i = 0; i< nombres.size(); i++){
                        nombres.remove(nombreCliente);
                    }
                    break;
                } else if (mensajeRecibido.equals("historial")) {
                    EnviarHistorial(socket);
                } else {
                    comun.setMensajes(comun.getMensajes() + mensajeRecibido + "\n");
                    EnviarMensajes(mensajeRecibido);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private synchronized void EnviarMensajes(String texto){
        //recorrer el chat de sockets para enviar los mensajes
        for(int i = 0; i< comun.getConexiones(); i++){
            Socket destino = comun.getElementoChat(i);
            if(!destino.isClosed()){
                try {
                    DataOutputStream salida = new DataOutputStream(destino.getOutputStream());
                    salida.writeUTF(texto);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
