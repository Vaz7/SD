package server;

import cmd.Connection;
import cmd.Job;
import cmd.Message;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientHandler implements Runnable{
    private Socket clientSocket;
    private Server server;
    private JobList jobList;
    SSQueue slaveServers;

    private int maxMem=0;

    public ClientHandler(Socket clientSocket, Server server, JobList jobList, SSQueue slaveServers) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.jobList = jobList;
        this.slaveServers = slaveServers;
        this.maxMem  = slaveServers.getMaxTotalMemory();
    }

    @Override
    public void run() {
        try {
            Connection con = new Connection(clientSocket);
            for ( ; ; ) {
                Message received = con.receiveMessage();
                int tag = received.getTag();

                int ret = messageManager(received);
                switch(ret){
                    case 1:
                        con.sendMessage(new Message((byte) 4, tag));
                        break;
                    case 3:
                        con.sendMessage(new Message((byte) 6, tag));
                        break;
                    case 2:
                        con.sendMessage(new Message((byte) 5, tag));
                        break;
                    case 4:
                        con.sendMessage(new Message((byte) 7, tag));
                        break;
                    case 5:
                        con.sendMessage(new Message((byte) 10, tag));
                        break;
                    case 6:
                        int size = this.jobList.size();
                        int memory = slaveServers.getTotalAvailableMem();
                        byte[] data = new byte[1];
                        data[0] = (byte) size;
                        con.sendMessage(new Message(data, (byte) 4, memory,tag));
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

                //para garantir que a mensagem não é maior que a maior memoria dos servidores


                if(tmp.getNum() > this.maxMem){
                    return 5;
                }

                Job job = new Job(tmp.getData(),tmp.getNum(), this.clientSocket, tmp.getTag());
                System.out.println("Job has " + job.getMemoria() + " bytes");
                jobList.addJob(job);
                System.err.println("Job added to queue");
                jobList.printQueue();

                break;
            case 4:
                return 6;
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
