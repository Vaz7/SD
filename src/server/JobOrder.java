package server;
import cmd.Job;

public class JobOrder {
    private Job job;
    private int memLeft;

    public JobOrder(Job job) {
        this.job = job;
        this.memLeft = job.getMemoria();
    }

    public int subtract(int quantum){
        int total = memLeft - quantum;
        if(total > 0)
            this.memLeft -= quantum;
        else this.memLeft = 0;
        return this.memLeft;
    }

    public Job getJob() {
        return job;
    }

    public int getMemLeft() {
        return memLeft;
    }
}
