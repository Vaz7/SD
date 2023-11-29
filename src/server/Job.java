package server;

import java.net.Socket;
import java.util.concurrent.locks.Condition;

public class Job {
    //bytes do programa
    private byte[] bytes;
    //memoria necessaria
    private int memoria;
    private Socket socket;

    public byte[] getBytes() {
        return bytes;
    }

    public int getMemoria() {
        return memoria;
    }

    public Socket getSocket() {
        return socket;
    }

    public Job(byte[] bytes, int memoria, Socket socket) {
        this.bytes = bytes;
        this.memoria = memoria;
        this.socket = socket;
    }

}
