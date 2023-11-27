package server;

import cmd.Connection;

import java.net.Socket;
import java.util.List;

public class JobManager implements Runnable {
    private Server server;
    private Connection con;

    public JobManager(Server server, Connection socket){
        this.server = server;
        this.con = socket;
    }
    @Override
    public void run() {
        while (true){
            Job c = server.execute();
            Thread exec = new Thread(new JobExecute(this.con, c, server));
            exec.start();
        }
    }
}
