package server;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private Map<String, byte[]> userInfo;
    private ReentrantReadWriteLock catalogo = new ReentrantReadWriteLock();
    private Lock writel = catalogo.writeLock();
    private Lock readl = catalogo.readLock();
    private ReentrantLock fifo = new ReentrantLock();
    private List<Job> jobQueue = new ArrayList<>();


    private class Memory {
        int quantity = 500;
        private Condition tooLow = fifo.newCondition();
    }
    private Memory mem = new Memory();

    public Server(){
        this.userInfo = new HashMap<>();
    }

    public void addJob(Job j){
        fifo.lock();
        try{
            jobQueue.add(j);
        }
        finally {
            fifo.unlock();
        }
    }
    public void printQueue(){
        System.out.println("the queue has " + this.jobQueue.size() + " elements!");
    }

    public void removeJob(){
        // @TODO
    }

    /**
     * Método que adiciona user à hash
     * @param id
     * @param pass
     */
    public void addUser(String id, byte[] pass){
        try{
            writel.lock();
            this.userInfo.put(id, pass);
        } finally {
            writel.unlock();
        }
    }

    /**
     * Método que devolve a password de um dado ID para verificar se corresponde
     * @param id
     * @return
     */
    public boolean isPassword(String id, byte[] pass){
        try{
            this.readl.lock();
            byte[] array = this.userInfo.get(id);
            return Arrays.equals(array, pass);
        } finally{
            this.readl.unlock();
        }
    }

    /**
     * Método que verifica se um ID está registado.
     * @param id
     * @return
     */
    public boolean containsID(String id){
        try{
            readl.lock();
            if(this.userInfo.containsKey(id)) return true;
            else return false;
        } finally {
            readl.unlock();
        }
    }

    public void addJob(){
        // @TODO
    }
}
