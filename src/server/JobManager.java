package server;

import java.net.Socket;
import java.util.List;

public class JobManager implements Runnable {
    private Server server;
    private Socket socket;

    public JobManager(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
    }
    @Override
    public void run() {
        while (true){
            server.isEmpty();
            Job c = server.getFirstElement();
            Thread exec = new Thread(new JobExecute(this.socket, c, server));
            exec.start();
            server.removeJob(c);
        }
    }
}
