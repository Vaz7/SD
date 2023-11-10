package cliente;

public class Main {
    public static void main(String[] args) throws Exception {



        MyClientSocket cliente = new MyClientSocket(1234);
        cliente.initCliente();
        Menu.menu(cliente);
        cliente.sendMessage("vasco paneleiro");
        cliente.close();

    }
}
