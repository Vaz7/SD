package SecundaryServer;

public class Main {

    public static void main(String[] args) {
        if(args.length==1){
            int porta = Integer.parseInt(args[0]);
            Secundary_Server secServer = new Secundary_Server(porta);
            secServer.initServer();
        }
        else{
            System.out.println("Please use only 1 argument with the port number!");
        }

    }


}
