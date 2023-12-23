package server;

import cmd.Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class JobManager implements Runnable {
    private Memory mem;
    private JobList jobList;

    private List<SSdata> slaveServers = new ArrayList<SSdata>();

    public JobManager(Memory mem,JobList jobList) throws UnknownHostException {
        this.mem = mem;
        this.jobList = jobList;
        SSdata s1 = new SSdata("127.0.0.1",12345);
        SSdata s2 = new SSdata("127.0.0.1",12113);
        slaveServers.add(s1);
        slaveServers.add(s2);
    }

    public void startCons(){
        for(SSdata s : slaveServers){
            try {
                Socket SSsocket = new Socket(s.getIp(),s.getPort());
                Connection con = new Connection(SSsocket);
                s.setConnection(con);
                s.setAvailableMem(con.readInt());
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        startCons();
        while (true){
            jobList.isEmpty();
            Job c = jobList.removeJob(this.mem);
            Thread exec = new Thread(new JobExecute(c, mem,slaveServers.getFirst().getCon()));
            exec.start();
        }
    }
}
