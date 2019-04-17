package Sockets.View;

import Sockets.Controller.Controle;

import java.util.ArrayList;
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
    }

    //adiciona e controla o tamanho do log
    public synchronized void adicionarLog(String mensagem) {
        //Adição de bloco de código syncronizado
        synchronized (this) {
            this.log.add(mensagem);
            //essa variave
            byte tamanhoMaxLog = 15;
            if (this.log.size() > tamanhoMaxLog)
                this.log.remove(0);
        }
    }

    //imrpime uma mensagem mantendo o tamanho da tela estavel
    private void impressaoLinhaAdaptavel(String mensagem) {
        System.out.print("| ");
        if (mensagem.length() <= tamanho)
            System.out.print(mensagem);
        else
            System.out.print(mensagem.substring(0, tamanho + 1));
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
        this.impressaoLinhaAdaptavel("Segundos absolutos: " + this.controle.relogioVirtual.getTempo());
        if (this.controle.processos.getEsteProcesso().getMaster())
            this.impressaoLinhaAdaptavel("Meu mestre: EU MESMO");
        else if (this.controle.processos.getMestre() != null)
            this.impressaoLinhaAdaptavel("Meu mestre: " + this.controle.processos.getMestre().getIdentificador());
        this.impressaoQuebraLinha();
        this.impressaoLinhaAdaptavel("\t -> Lista de processos descobertos:");
        for (int contador = 0; contador < this.controle.processos.getNumeroProcessos(); contador++)
            this.impressaoLinhaAdaptavel("Processo " + contador + ": " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
        this.impressaoQuebraLinha();
        this.impressaoLinhaAdaptavel("\t -> Lista de logs:");

        //Adição de bloco de código syncronizado
        synchronized (this) {
            for (int contador = 0; contador < this.log.size(); contador++) {
                this.impressaoLinhaAdaptavel(this.log.get(contador));
            }
        }
        this.impressaoQuebraLinha();
    }
}
