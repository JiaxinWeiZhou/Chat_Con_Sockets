package TCP;

import java.net.Socket;

public class HiloComun {
    int conexiones;
    int actuales;
    int maximo;
    Socket[] chat = new Socket[maximo];
    String mensajes;

    public HiloComun(int maximo, Socket[] chat){
        this.maximo = maximo;
        this.actuales = 0;
        this.conexiones = 0;
        this.chat = chat;
        mensajes= "";
    }
    public HiloComun() {}

    public synchronized int getConexiones() {
        return conexiones;
    }

    public synchronized void setConexiones(int conexiones) {
        this.conexiones = conexiones;
    }

    public synchronized int getActuales() {
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

    public synchronized String getMensajes() {
        return mensajes;
    }

    public synchronized void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public synchronized void addChat(Socket s, int i){
        chat[i] = s;
    }
    public synchronized Socket getElementoChat(int i){
        return chat[i];
    }
}
