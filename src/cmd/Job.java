package cmd;

import java.net.Socket;
import java.util.concurrent.locks.Condition;

public class Job {
    //bytes do programa
    private byte[] bytes;
    //memoria necessaria
    private int memoria;
    private int tag;
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

    public int getTag() {
        return tag;
    }

    public Job(byte[] bytes, int memoria, Socket socket, int tag) {
        this.bytes = bytes;
        this.memoria = memoria;
        this.socket = socket;
        this.tag = tag;
    }



}
