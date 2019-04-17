package Sockets.Controller.Disparadores;

import Sockets.Controller.Controle;

import java.util.Timer;
import java.util.TimerTask;

public class VerificaMestre extends TimerTask {

    //Variável para a injeção de dependencia
    private final Controle controle;

    //Essa variável irá armazenar a tarefa a ser diparada de tempos em tempos
    private final Timer disparador;

    private Boolean masterAtrasado;

    public VerificaMestre(Controle controle, Long deltaTempo) {
        this.controle = controle;

        this.disparador = new Timer();

        this.masterAtrasado = true;

        this.disparador.schedule(this, deltaTempo, deltaTempo);
    }


    //Essa função mata a schedule
    public void matarDisparador() {
        this.disparador.cancel();
    }

    //Essa função indica que o master se comunicou com o processo
    public void mestreDisponivel() {
        this.masterAtrasado = false;
    }

    public void run() {
        //Isso verifica se o mestre esta atrasado, ou seja, não se comunicou no periodo deltaTempo
        if (this.masterAtrasado) {
            this.controle.tela.adicionarLog("Mestre foi morto por estar atrasado");
            this.controle.processos.novoMestre();

            if (this.controle.processos.getEsteProcesso().getMaster()){
                this.controle.virarMestre();
                this.controle.tela.adicionarLog("Eu sou o novo mestre");
                this.disparador.cancel();
                this.disparador.purge();
            } else
                this.controle.tela.adicionarLog("Novo mestre selecionado");

        } else {
            this.masterAtrasado = true;
        }
    }
}
