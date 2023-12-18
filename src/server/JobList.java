package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JobList {
    private List<Job> jobQueue = new ArrayList<>();
    private ReentrantReadWriteLock fifo = new ReentrantReadWriteLock();
    private Lock writefifo = fifo.writeLock();
    private Lock readfifo = fifo.readLock();

    private ReentrantLock l = new ReentrantLock();
    private Condition isEmpty = l.newCondition();
    private Condition canExecute = l.newCondition();

    public void printQueue(){
        System.out.println("the queue has " + this.jobQueue.size() + " elements!");
    }

    public Job removeJob(int availableMemory){
        writefifo.lock();
        l.lock();
        Job res;
        try{
            while(jobQueue.get(0).getMemoria() > availableMemory){
                canExecute.await();
            }
            res = jobQueue.get(0);
            jobQueue.removeFirst();
            return res;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            writefifo.unlock();
            l.unlock();
        }
    }

    public void addJob(Job j){
        writefifo.lock();
        l.lock();
        try{
            jobQueue.add(j);
            isEmpty.signal();
        } finally {
            writefifo.unlock();
            l.unlock();
        }
    }
    public void isEmpty(){
        l.lock();
        try{
            while(this.jobQueue.isEmpty()){
                isEmpty.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            l.unlock();
        }
    }

    public int size(){
        l.lock();
        try{
            return this.jobQueue.size();
        } finally {
            l.unlock();
        }
    }
}
