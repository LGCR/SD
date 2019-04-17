package Sockets.Controller;


import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RelogioVirtual extends TimerTask {

    //variável que armazenará o tempo do relógio digital
    private Long segundos;

    @SuppressWarnings("deprecation")
    public RelogioVirtual(Boolean aleatoriedade) {

        //Aqui iniciamos os segundos do relógio com o tempo atual do sistema
        Date dataSistema = new Date(System.currentTimeMillis());
        this.segundos = dataSistema.getHours() * 3600L + dataSistema.getMinutes() * 60L + dataSistema.getSeconds();

        //caso o parâmetro passado pelo construtor o rellógio digital irá iniciar com um valor aleatório entre +600 e -600 segundo
        if (aleatoriedade) {
            this.segundos += (new Random()).nextInt() % 1200 - 600;
        }

        //Inicia a thred de horario
        new Timer().schedule(this, 1000, 1000);
    }

    //essa função retorna as horas
    private Long getHoras() {
        return this.segundos / 3600L;
    }

    //essa função retorna os minutos
    private Long getMinutos() {
        return (this.segundos - (this.getHoras() * 3600L)) / 60L;
    }

    //essa função retorna os segundos
    private Long getSegundos() {
        return this.segundos - (this.getHoras() * 3600L) - (this.getMinutos() * 60L);
    }

    //essa função irá retornar o tempo no formato hora:minuto:segundo
    public String getHorario() {
        return "" + this.getHoras() + ":" + this.getMinutos() + ":" + this.getSegundos();
    }

    public Long getTempo() {
        return this.segundos;
    }


    //função realizar soma na variável de tempo, serve para realizar ajustes e incrementar o tempo
    public synchronized void somarTempo(Long segundos) {
        this.segundos += segundos;
        if (this.segundos >= 86400L)
            this.segundos += -86400L * (this.segundos / 86400L);

    }

    public static String converteMilesegundosParaHorario(Long milesegundos) {
        String horario = "";

        horario += "" + milesegundos / 3600L + ":";
        horario += "" + (milesegundos - (milesegundos / 3600L) * 3600L) / 60L + ":";
        horario += "" + (milesegundos - ((milesegundos - (milesegundos / 3600L) * 3600L) / 60L) * 60L - ((milesegundos / 3600L) * 3600L));


        return horario;
    }

    @Override
    public void run() {
        //essa chamada de função é feita a cada um segundo
        somarTempo(1L);
    }
}
