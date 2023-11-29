package cliente;

import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws Exception {

        if(args.length==0){
            Client cliente = new Client();
            cliente.connection();
        }

        //test mode
        else{
            Client cliente = new Client();
            cliente.connectionTest(args[0],Integer.parseInt(args[1]));
        }

        Scanner sc = new Scanner(System.in);
        sc.nextLine();

    }
}
