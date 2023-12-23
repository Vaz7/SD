package server;

import cmd.Job;
import cmd.Memory;

public class JobManager implements Runnable {
    private Memory mem;
    private JobList jobList;
    private SSQueue slaveServers;

    public JobManager(JobList jobList, SSQueue slaveServers){
        this.jobList = jobList;
        this.slaveServers = slaveServers;
    }
    @Override
    public void run() {
        while (true){
            jobList.isEmpty();
            SSdata chosenOne = slaveServers.removeChosen();
            Job c = jobList.removeJob(chosenOne.getMem());
            slaveServers.addServer(chosenOne);
            Thread exec = new Thread(new JobExecute(c,chosenOne.getCon(),chosenOne.getMem()));
            exec.start();
        }
    }
}
