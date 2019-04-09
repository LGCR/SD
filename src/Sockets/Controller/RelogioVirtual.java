package Sockets.Controller;


import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RelogioVirtual extends TimerTask {

    //variável que armazenará o tempo do relógio digital
    private int segundos;

    public RelogioVirtual(Boolean aleatoriedade) {

        //Aqui iniciamos os segundos do relógio com o tempo atual do sistema
        Date dataSistema = new Date(System.currentTimeMillis());
        this.segundos = dataSistema.getHours() * 3600 + dataSistema.getMinutes() * 60 + dataSistema.getSeconds();

        //caso o parâmetro passado pelo construtor o rellógio digital irá iniciar com um valor aleatório entre +60 e -60 segundo
        if (aleatoriedade) {
            this.segundos += (new Random()).nextInt() % 120 - 120;
        }

        //Inicia a thred de horario
        new Timer().schedule(this, 1000, 1000);
    }

    //essa função retorna as horas
    public int getHoras() {
        return this.segundos / 3600;
    }

    //essa função retorna os minutos
    public int getMinutos() {
        return (this.segundos - (this.getHoras() * 3600)) / 60;
    }

    //essa função retorna os segundos
    public int getSegundos() {
        return this.segundos - (this.getHoras() * 3600) - (this.getMinutos() * 60);
    }

    //essa função irá retornar o tempo no formato hora:minuto:segundo
    public String getHorario() {
        return "" + this.getHoras() + ":" + this.getMinutos() + ":" + this.getSegundos();
    }

    public int getTempo(){
        return this.segundos;
    }


    //função realizar soma na variável de tempo, serve para realizar ajustes e incrementar o tempo
    public synchronized void somarTempo(int segundos) {
        this.segundos += segundos;
        if (this.segundos >= 86400)
            this.segundos += -86400 * (this.segundos/86400);

    }

    @Override
    public void run() {
        //essa chamada de função é feita a cada um segundo
        somarTempo(1);
    }
}
