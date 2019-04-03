package Sockets.Controller;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimerTask;

public class RelogioDigital extends TimerTask {

    //variável que armazenará o tempo do relógio digital
    private Long miliSegundos;
    private Long taxaAtualizacao;

    public RelogioDigital(@NotNull Boolean aleatoriedade, Long refreshRate) {

        //Aqui iniciamos os milisegudnos do relógio com o tempo atual do sistema
        this.miliSegundos = System.currentTimeMillis();

        //caso o parâmetro passado pelo construtor o rellógio digital irá iniciar com um valor aleatório entre +1 e -1 segundo
        if (aleatoriedade) {
            this.miliSegundos += (new Random()).nextLong() % 1000 - 1;
        }

        this.taxaAtualizacao = refreshRate;
    }

    //essa função retorna as horas
    public int getHoras() {
        return this.getDataAtual().getHours();
    }

    //essa função retorna os minutos
    public int getMinutos() {
        return this.getDataAtual().getMinutes();
    }

    //essa função retorna os segundos
    public int getSegundos() {
        return this.getDataAtual().getSeconds();
    }

    //essa função irá retornar o tempo no formato hora:minuto:segundo
    public String getHorario() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date (this.miliSegundos));
    }

    //retorna o tempo do relógio em milesegundos
    public Long getTempo(){
        return this.miliSegundos;
    }

    public Long getTaxaAtualizacao(){
        return this.taxaAtualizacao;
    }

    // converte o tempo em milisegundos em um tipo Date
    private Date getDataAtual() {
        return new Date(this.miliSegundos);
    }

    //função realizar soma na variável de tempo, serve para realizar ajustes e incrementar o tempo
    synchronized public void somarTempo(Long millisegundos) {
        this.miliSegundos += miliSegundos;
    }

    @Override
    public void run() {
        //essa chamada de função é feita a cada um segundo
        somarTempo(this.taxaAtualizacao);
    }
}
