package cliente;

public class Main {
    public static void main(String[] args) throws Exception {

        //Menu.menu();

        MyClientSocket cliente = new MyClientSocket(1234);
        cliente.initCliente();
        cliente.sendMessage("vasco paneleiro");
        cliente.close();

    }
}
