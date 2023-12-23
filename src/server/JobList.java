package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JobList {
    private List<JobOrder> jobQueue = new ArrayList<>();
    private ReentrantReadWriteLock fifo = new ReentrantReadWriteLock();
    private Lock writefifo = fifo.writeLock();
    private Lock readfifo = fifo.readLock();

    private static ReentrantLock l = new ReentrantLock();
    private static Condition canExecute = l.newCondition();
    private static Condition canExecutePrio = l.newCondition();
    private ReentrantLock l2 = new ReentrantLock();
    private Condition isEmpty = l2.newCondition();
    private int quantum = 2;
    private static Set<JobOrder> priority = new HashSet<>();
    public void printQueue(){
        System.out.println("the queue has " + this.jobQueue.size() + " elements!");
    }

    public Job removeJob(Memory mem){

        l.lock();
        try{
            while(true){
                JobOrder res;
                res = getFirst();
                while(quantum > mem.getAvailableMemory()){
                    canExecute.await();
                }

                int left = res.subtract(this.quantum);
                if(left > 0){
                    removeFirst();
                    this.jobQueue.add(res);
                }
                else{
                    while(res.getJob().getMemoria() > mem.getAvailableMemory()){
                        priority.add(res);
                        canExecutePrio.await();
                    }
                    priority.remove(res);
                    removeFirst();
                    mem.updateMem(-res.getJob().getMemoria());
                    return res.getJob();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l.unlock();
        }
    }

    public void addJob(JobOrder j){
        writefifo.lock();
        l2.lock();
        try{
            jobQueue.add(j);
            isEmpty.signalAll();
        } finally {
            writefifo.unlock();
            l2.unlock();
        }
    }
    public void isEmpty(){
        l2.lock();
        try{
            while(this.jobQueue.isEmpty()){
                isEmpty.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l2.unlock();
        }
    }

    public int size(){
        readfifo.lock();
        try{
            return this.jobQueue.size();
        } finally {
            readfifo.unlock();
        }
    }

    public static void freeCond(){
        try {
            l.lock();
            if(priority.isEmpty())
                canExecute.signalAll();
            else
                canExecutePrio.signalAll();
        }
        finally {
            l.unlock();
        }
    }

    private void removeFirst(){
        try{
            writefifo.lock();
            this.jobQueue.removeFirst();
        }
        finally {
            writefifo.unlock();
        }
    }
    private JobOrder getFirst(){
        try {
            writefifo.lock();
            return this.jobQueue.getFirst();
        }
        finally {
            writefifo.unlock();
        }
    }
}
