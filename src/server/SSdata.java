package server;

import cmd.Connection;

import java.net.UnknownHostException;

public class SSdata {
    String ip;
    int port;
    Memory mem;
    Connection con;

    public SSdata(String ip, int port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }

    public void setAvailableMem(int availableMem){
        this.mem = new Memory(availableMem);
    }

    public void setConnection(Connection con){
        this.con = con;
    }

    public String getIp(){
        return ip;
    }

    public int getPort() {
        return port;
    }
    public Connection getCon(){
        return this.con;
    }
}
