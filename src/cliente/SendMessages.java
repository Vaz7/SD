package cliente;

import java.io.IOException;

public class SendMessages {

    public static boolean sendAuthenticationRequest(String username, String password,MyClientSocket servidor) {
        try{
            servidor.sendMessage("0");
            servidor.sendMessage(username);
            servidor.sendMessage(password);
        }
        catch (Exception e){
            throw new RuntimeException();
        }
        return true;
    }

    public static boolean sendRegistationRequest(String username, String password,MyClientSocket servidor) {
        try{
            servidor.sendMessage("1");
            servidor.sendMessage(username);
            servidor.sendMessage(password);
        }
        catch (Exception e){
           throw new RuntimeException();
        }
        return true;
    }

    public static void sendCode (String filepath,MyClientSocket servidor){
        //aqui vai enviar o codigo a executar
        System.out.println("A executar o ficheiro " + filepath + " !!!!!!!!!!!!!!!");
    }
}
