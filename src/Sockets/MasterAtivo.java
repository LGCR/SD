package Sockets;

import java.util.TimerTask;

public class MasterAtivo extends TimerTask {

    String id;

    MasterAtivo(String id){
        this.id = id;
    }

    @Override
    public void run() {
        new Multicast.Send("master ativo/" + id);
    }
}
