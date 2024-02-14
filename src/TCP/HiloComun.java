package TCP;

import java.net.Socket;

public class HiloComun {
    int conexiones;
    int actuales;
    int maximo;
    Socket chat[] = new Socket[maximo];
    String mensajes;

    public HiloComun(int maximo, int actuales, int conexiones, Socket chat[]){
        this.maximo = maximo;
        this.actuales = actuales;
        this.conexiones = conexiones;
        this.chat = chat;
        mensajes= "";
    }
    public HiloComun() {}

    public int getConexiones() {
        return conexiones;
    }

    public synchronized void setConexiones(int conexiones) {
        this.conexiones = conexiones;
    }

    public int getActuales() {
        return actuales;
    }

    public synchronized void setActuales(int actuales) {
        this.actuales = actuales;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public String getMensajes() {
        return mensajes;
    }

    public synchronized void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public synchronized void addChat(Socket s, int i){
        chat[i] = s;
    }
    public Socket getElementoChat(int i){
        return chat[i];
    }
}
