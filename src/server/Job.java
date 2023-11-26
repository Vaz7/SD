package server;

public class Job {
    //bytes do programa
    byte[] bytes;
    //memoria necessaria
    int memoria;


    public Job(byte[] bytes, int memoria) {
        this.bytes = bytes;
        this.memoria = memoria;
    }

}
