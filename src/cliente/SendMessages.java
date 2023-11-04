package cliente;

public class SendMessages {

    public static boolean sendAuthenticationRequest(String username, String password){
        //manda ao server as cenas e dps retorna 1 se esta autenticado e 0 se nao esta
        return true;
    }

    public static boolean sendRegistationRequest(String username, String password){
        //manda ao server as cenas e dps retorna 1 se esta registado e 0 se nao esta
        return true;
    }

    public static void sendCode (String filepath){
        //aqui vai enviar o codigo a executar
        System.out.println("A executar o ficheiro " + filepath + " !!!!!!!!!!!!!!!");
    }
}
