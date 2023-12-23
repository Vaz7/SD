package server;

import cmd.Connection;
import cmd.Message;
import sd23.JobFunction;
import sd23.JobFunctionException;

import java.io.*;
import java.net.Socket;

public class JobExecute implements Runnable {
    private Job job;
    private Memory mem;
    private Connection con;

    public JobExecute(Job job,Connection con,Memory mem) {
        this.job = job;

        //connection para o SS
        this.con = con;
        this.mem = mem;
    }

    @Override
    public void run() {
        Socket clientSocket = job.getSocket();
        Connection conWithClient = null;
        try {
            conWithClient = new Connection(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int meem = job.getMemoria();
            Message mes = new Message(job.getBytes(),(byte)3,job.getMemoria(),job.getTag());
            con.sendMessage(mes);

            Message recievedFromSS = con.receiveMessage();

            mem.updateMem(meem);
            System.err.println("[" + con.getSocket().toString() + "]" + " returned " + recievedFromSS.getData().length + " bytes");

            if(recievedFromSS.getMsg()==9){
                conWithClient.sendMessage(new Message((byte) 9, this.job.getTag()));
            }
            else{
                conWithClient.sendMessage(new Message(recievedFromSS.getData(), (byte) 8, 0, this.job.getTag()));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
