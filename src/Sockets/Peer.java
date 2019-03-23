package Sockets;

import java.util.ArrayList;

public class Peer {
    String id;
    String ip;
    String publicKey;
    String privateKey;
    String master;
    ArrayList<String> slaves;

    Peer(){
        //setId(id);
        slaves = new ArrayList<>();
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

    void setSlave(String slave){
        slaves.add(slave);
    }

    String getSlave(int index){
        return slaves.get(index);
    }

    boolean hasSlave(String slave){
        for (int i = 0; i < getSlaveSize(); i++){
            if (slaves.get(i).equals(slave)){
                return true;
            }
        }
        return false;
    }

    int getSlaveSize(){return slaves.size();}

}
