package Sockets.View;

import Sockets.Controller.Controle;
import Sockets.Model.Processo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Tela extends TimerTask {
    private Controle controle;

    //essa variável guarda o tamanho da tela a ser apresentada
    private static byte tamanho = 50;

    //essa variávell guarda as ultimas mensagens do processo
    private ArrayList<String> log;

    public Tela(Controle controle) {
        //injeção de dependencia
        this.controle = controle;

        //instancia do log
        this.log = new ArrayList<>();

        //iniciando tela
        new Timer().schedule(this, 1000L, 1000L);
    }

    //adiciona e controla o tamanho do log
    public synchronized void adicionarLog(String mensagem) {
        this.log.add(mensagem);
        //essa variave
        byte tamanhoMaxLog = 15;
        if (this.log.size() > tamanhoMaxLog)
            this.log.remove(0);
    }

    //imrpime uma mensagem mantendo o tamanho da tela estavel
    private void impressaoLinhaAdaptavel(String mensagem) {
        System.out.print("| ");
        if (mensagem.length() <= tamanho)
            System.out.print(mensagem);
        else
            System.out.print(mensagem.substring(0, tamanho));
        for (int contador = 0; contador <= tamanho - mensagem.length(); contador++)
            System.out.print(" ");
        System.out.println(" |");
    }

    //imprime uma quebra de linha do tamanho da tela
    private void impressaoQuebraLinha() {
        for (byte contador = 0; contador <= tamanho + 4; contador++) {
            System.out.print("=");
        }
        System.out.println("");
    }


    @Override
    public void run() {

        this.impressaoQuebraLinha();
        this.impressaoLinhaAdaptavel("MEU ID: " + this.controle.processos.getEsteProcesso().getIdentificador());
        this.impressaoLinhaAdaptavel("Horário: " + this.controle.relogioVirtual.getHorario());
        this.impressaoLinhaAdaptavel("Sou Mestre?: " + this.controle.processos.getEsteProcesso().getMaster());
        this.impressaoQuebraLinha();
        this.impressaoLinhaAdaptavel("\t -> Lista de processos descobertos:");
        ArrayList<Processo> lista = this.controle.processos.getCopiaListaProcessos();
        for (int contador = 0; contador < lista.size(); contador++)
            this.impressaoLinhaAdaptavel("Processo de ID: " + lista.get(contador).getIdentificador());
        this.impressaoQuebraLinha();
        this.impressaoLinhaAdaptavel("\t -> Lista de logs:");
        for (int contador = this.log.size() - 1; contador >= 0; contador--) {
            this.impressaoLinhaAdaptavel(this.log.get(contador));
        }
        this.impressaoQuebraLinha();
    }
}
