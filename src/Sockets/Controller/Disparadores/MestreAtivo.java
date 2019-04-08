package Sockets.Controller.Disparadores;

import Sockets.Controller.Controle;
import Sockets.Model.PacoteMensagem;

import java.util.Timer;
import java.util.TimerTask;

public class MestreAtivo extends TimerTask {

    private Controle controle;

    MestreAtivo(Controle controle){
        this.controle = controle;
        Timer timer = new Timer();
        timer.schedule(this, 1000, 1000);
    }

    @Override
    public void run() {
        new PacoteMensagem(controle.processos.getEsteProcesso().getIdentificador(),
                PacoteMensagem.DISPONIVEL, null);
    }
}
