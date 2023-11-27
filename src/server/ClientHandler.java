package server;

import cmd.Connection;
import cmd.Message;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientHandler implements Runnable{
    private Connection con;
    private Server server;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.con = new Connection(clientSocket);
        this.server = server;
    }

    @Override
    public void run() {
        try {
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
                    case 5:
                        con.sendMessage(new Message((byte) 10));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
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
    private int messageManager(Message tmp) throws InterruptedException {
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
                if(tmp.getNum() > 500){
                    return 5;
                }
                Job job = new Job(tmp.getData(),tmp.getNum());
                server.addJob(job);
                Thread jobmanager = new Thread(new JobManager(server, con));
                jobmanager.start();
                jobmanager.join();

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
