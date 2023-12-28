package server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SSQueue {
    private PriorityQueue<SSdata> slaveServers = new PriorityQueue<>(new SSComparator());
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writel = lock.writeLock();
    private Lock readl = lock.readLock();


    public void addServer(SSdata ss){
        writel.lock();
        try{
            slaveServers.add(ss);
        }
        finally {
            writel.unlock();
        }
    }

    public SSdata removeChosen(){
        writel.lock();
        try {
            return slaveServers.poll();
        }
        finally {
            writel.unlock();
        }
    }


    public int getTotalAvailableMem(){
        int tot=0;
        List<SSdata> ssdatas = new ArrayList<>();
        readl.lock();
        try {
            ssdatas.addAll(slaveServers);
        } finally {
            readl.unlock();
        }
        for(SSdata s : ssdatas){
            tot += s.getAvailableMem();
        }
        return tot;
    }

    public int getMaxTotalMemory(){
        int max = 0;
        readl.lock();
        try{
            for(SSdata s : slaveServers){
                int current = s.mem.getTotalMemory();
                if(current>max){
                    max = current;
                }
            }
            return max;
        }
        finally {
            readl.unlock();
        }
    }
    class SSComparator implements Comparator<SSdata> {
        @Override
        public int compare(SSdata o1, SSdata o2){
            int o1mem = o1.getMem().getAvailableMemory();
            int o2mem = o2.getMem().getAvailableMemory();
            return Integer.compare(o2mem, o1mem);
        }
    }
}
