package Sockets;

import java.util.Date;
import java.util.TimerTask;

public class Tempo extends TimerTask {

    private int horas;
    private int minutos;
    private int segundos;

    public int getHoras() {
        return horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    Tempo(){
        Date date = new Date();
        horas = date.getHours();
        minutos = date.getMinutes();
        segundos = date.getSeconds();
    }

    synchronized public void somarTempo(int millisegundos){
        segundos += (int) (millisegundos / 1000) % 60 ;
        minutos += (int) ((millisegundos / (1000*60)) % 60);
        horas += (int) ((millisegundos / (1000*60*60)) % 24);

        if (segundos == 60){
            segundos = 0;
            minutos++;
        }
        if (minutos == 60){
            minutos = 0;
            horas++;
        }
        if (horas == 24){
            horas = 0;
        }
    }

    @Override
    public void run() {
        somarTempo(1000);
        //System.out.println(getHoras() + ":" + getMinutos() + ":" + getSegundos());
    }
}
