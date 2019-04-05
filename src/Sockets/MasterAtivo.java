package Sockets;

import Sockets.Controller.Controle;

import java.util.TimerTask;

public class MasterAtivo extends TimerTask {

    private final String id;
    private final Controle controle;

    public MasterAtivo(String id, Controle controle) {
        this.controle = controle;
        this.id = id;

    }

    @Override
    public void run() {
        this.controle.controleMulticast.enviarMensagem(("master ativo/" + id).getBytes());
    }
}
