package Sockets.Model;

import java.util.ArrayList;

public class Processo {
    String id;
    String ip;
    int porta;
    String publicKey;
    String privateKey;
    String master;
    ArrayList<Processo> processos;

    public Processo(String pid, String master) {
        setId(pid);
        setMaster(master);
        processos = new ArrayList<>();
    }

    String getId() {
        return this.id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getIp() {
        return this.ip;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    String getMaster() {
        return this.master;
    }

    void setMaster(String master) {
        this.master = master;
    }
}
