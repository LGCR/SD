package Sockets;

import java.util.ArrayList;

public class Processo {
    String id;
    String ip;
    int porta;
    String publicKey;
    String privateKey;
    String master;
    ArrayList<Processo> processos;

    Processo(String pid, String master){
        setId(pid);
        setMaster(master);
        processos = new ArrayList<>();
    }

    void setId(String id){
        this.id = id;
    }
    String getId(){return this.id;}

    void setIp(String ip){
        this.ip = ip;
    }
    String getIp(){return this.ip;}

    void setMaster(String master){
        this.master = master;
    }
    String getMaster(){return this.master;}
}
