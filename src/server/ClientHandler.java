package server;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            Connection con = new Connection(clientSocket);
            for ( ; ; ) {
                Message received = con.receiveMessage();
                int ret = messageManager(received);
                switch(ret){
                    case 1:
                        con.sendMessage(new Message((byte) 4));
                        break;
                    case 3:
                        con.sendMessage(new Message((byte) 6));
                        break;
                    case 2:
                        con.sendMessage(new Message((byte) 5));
                        break;
                    case 4:
                        con.sendMessage(new Message((byte) 7));
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        /**
     * Método para responder a cada mensagem em específico
     * @param tmp
     * @return 0 -> é para dar exit e o utilizador fazer logout
     *         1 -> autenticação errada
     *         2 -> username já criado
     *        -1 -> tudo certo, prosseguir
     */
    private int messageManager(Message tmp){
        // TO DO
        int caso = (int) tmp.getMsg();
        String idPassword;
        String[] parts;
        switch(caso){
            case 0:
                return 0;

            case 1:
                idPassword = new String(tmp.getData());
                parts = idPassword.split(" ");
                if(!this.server.isPassword(parts[0], sha1(parts[1]))) return 1;
                else return 2;

            case 2:
                // Juntar o id e password apenas numa string separada por um espaço
                idPassword = new String(tmp.getData());
                parts = idPassword.split(" ");
                if(this.server.containsID(parts[0])) return 3;
                this.server.addUser(parts[0], sha1(parts[1]));
                return 4;

            case 3:
                // TO DO:


                // aqui vai apenas adicionar a uma "lista"
                // Método de listar o código com a memória e criar algoritmo de escolha
                // utilizar conditions
                byte[] output;
                boolean flag = false;
                while(!flag){
                    try {
                        output = JobFunction.execute(tmp.getData());
                        flag = true;
                        System.err.println("success, returned "+output.length+" bytes");
                    } catch (JobFunctionException e) {
                        System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
                    }
                }

                break;
        }
        return -1;
    }

    /**
     * Método que converte uma String para SHA-1 (não vale a pena meter bcrypt)
     * @param input
     * @return
     */
    private static byte[] sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
