package server;

public class Main {
    public static void main(String[] args) throws Exception {
        MyServerSocket servidor = new MyServerSocket(1234);
        servidor.initServer();
        servidor.close();

    }
}
