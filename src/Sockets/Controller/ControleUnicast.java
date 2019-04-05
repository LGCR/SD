package Sockets.Controller;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ControleUnicast implements Runnable{

    private DatagramSocket unicastSocket;


    public ControleUnicast() {
        try {
            this.unicastSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Falha ao iniciar soket unicast! Finalizando processo");
            System.exit(1);
        }
    }

    @Override
    public void run() {

    }
}