package server;

public class Main {
    public static void main(String[] args) throws Exception {
        int mainServerPort = 1234;

        if(args.length==0)
            System.out.println("Need at least one secundary server!");
        //como recebe sempre IP e porta tem de ser par
        else if((args.length & 1) == 0){

            Server_Protocol servidor = new Server_Protocol(mainServerPort,args);
            servidor.initServer();
        }
        else{
            System.out.println("Please add the ip adress and port of each secundary server!");
        }
    }
}
