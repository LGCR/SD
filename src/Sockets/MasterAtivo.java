package Sockets;

import Sockets.Controller.Controle;
import Sockets.Controller.ControleMulticast;

import java.util.TimerTask;

public class MasterAtivo extends TimerTask {

    String id;
    Controle controle;

    public MasterAtivo(String id, Controle controle) {
        this.controle = controle;
        this.id = id;

    }

    @Override
    public void run() {
        this.controle.controleMulticast.enviarMensagem(("master ativo/" + id).getBytes());
    }
}
