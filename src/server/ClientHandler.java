package server;

import cmd.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run(){
        try{
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            byte[] buffer = new byte[4000];
            int bytesRead;
            List<Message> tmp = new ArrayList<>();
            int ret = -1;

            while ((bytesRead = in.read(buffer)) != -1) {

                int offset = bytesRead;
                for (int i = 0; i < bytesRead; i += 1095) {
                    int blockSize = Math.min(1095, offset);
                    byte[] receivedData = Arrays.copyOfRange(buffer, i, i + blockSize);
                    tmp.add(Message.deserializeMessage(receivedData));
                    System.out.println(i);
                    offset -= blockSize;
                }

                Message data = tmp.get(tmp.size()-1);

                // mensagem ACK para nao encher o buffer
                if(data.getMsg() == (byte) 3) out.write(Message.serializeMessage(new Message((byte) 10)));
                if(data.isLast()){
                    ret = messageManager(tmp);
                    tmp.clear();
                }


                switch(ret){
                    case 1:
                        out.write(Message.serializeMessage(new Message((byte) 4)));
                        out.flush();
                        break;
                    case 3:
                        out.write(Message.serializeMessage(new Message((byte) 6)));
                        out.flush();
                        break;
                    case 2:
                        out.write(Message.serializeMessage(new Message((byte) 5)));
                        out.flush();
                        break;
                    case 4:
                        out.write(Message.serializeMessage(new Message((byte) 7)));
                        out.flush();
                        break;
                }

                ret = -1;
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
    private int messageManager(List<Message> tmp){
        // TO DO
        int caso = (int) tmp.get(0).getMsg();
        String idPassword;
        String[] parts;
        switch(caso){
            case 0:
                return 0;

            case 1:
                idPassword = new String(Message.convertMessagesToByteArray(tmp));
                parts = idPassword.split(" ");
                if(!this.server.isPassword(parts[0], sha1(parts[1]))) return 1;
                else return 2;

            case 2:
                // Juntar o id e password apenas numa string separada por um espaço
                idPassword = new String(Message.convertMessagesToByteArray(tmp));
                parts = idPassword.split(" ");
                if(this.server.containsID(parts[0])) return 3;
                this.server.addUser(parts[0], sha1(parts[1]));
                return 4;

            case 3:
                // TO DO:


                // aqui vai apenas adicionar a uma "lista"
                // Método de listar o código com a memória e criar algoritmo de escolha
                // utilizar conditions
                System.out.println(new String(Message.convertMessagesToByteArray(tmp)));
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
