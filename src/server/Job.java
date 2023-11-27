package server;

import java.net.Socket;
import java.util.concurrent.locks.Condition;

public class Job {
    //bytes do programa
    private byte[] bytes;
    //memoria necessaria
    private int memoria;

    public byte[] getBytes() {
        return bytes;
    }

    public int getMemoria() {
        return memoria;
    }

    public Job(byte[] bytes, int memoria) {
        this.bytes = bytes;
        this.memoria = memoria;
    }

}
