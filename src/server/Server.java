package server;

import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private UserInfoManagement userInfo;

    public Server(){
        this.userInfo = new UserInfoManagement();
    }

    public void addUser(String id, byte[] pass){
        this.userInfo.addUser(id,pass);
    }

    public boolean isPassword(String id, byte[] pass){
        return this.userInfo.isPassword(id,pass);
    }

    public boolean containsID(String id){
        return this.userInfo.containsID(id);
    }

}
