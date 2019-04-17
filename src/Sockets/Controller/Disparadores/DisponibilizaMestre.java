package Sockets.Controller.Disparadores;

import Sockets.Controller.Controle;
import Sockets.Model.Mensagem.Mensagem;
import Sockets.Model.PacoteMensagem;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class DisponibilizaMestre extends TimerTask {

    private final Controle controle;

    private final Timer disparador;

    private final Long deltaTempo;

    public DisponibilizaMestre(Controle controle, Long deltaTempo) {
        this.controle = controle;
        this.disparador = new Timer();
        this.deltaTempo = deltaTempo;
    }

    public void disponibilizarMestre() {
        this.disparador.schedule(this, 1L, this.deltaTempo / 4);
    }

    @Override
    public void run() {
        boolean erro = false;

        do {
            try {
                this.controle.controleMulticast.enviarMensagem(
                        PacoteMensagem.convertePacoteMensagemParaArrayBytes(
                                new PacoteMensagem(
                                        this.controle.processos.getEsteProcesso().getIdentificador(),
                                        PacoteMensagem.DISPONIVEL,
                                        Mensagem.converteMensagemParaArrayBytes(
                                                new Mensagem(
                                                        this.controle.processos.getEsteProcesso().getChavePublica()
                                                )
                                        )
                                )
                        )
                );
            } catch (IOException e) {
                this.controle.tela.adicionarLog("Erro na mensagem de mestre disponivel");
                e.printStackTrace();
                erro = true;
            }
        }while (erro);
    }
}
