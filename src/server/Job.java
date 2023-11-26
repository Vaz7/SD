package server;

public class Job {
    //bytes do programa
    byte[] bytes;
    //memoria necessaria
    int memoria;

    //Qual a fila de prioridades que vai ficar
    int queue;

    public Job(byte[] bytes, int memoria, int queue) {
        this.bytes = bytes;
        this.memoria = memoria;
        this.queue = queue;
    }

}
