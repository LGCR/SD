package Sockets;

import Sockets.Controller.Controle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Multicast {

    private static int port;
    private static InetAddress group;
    private static MulticastSocket socket;
    private String mensagem;

    public Multicast(int port, InetAddress group, MulticastSocket socket, Controle controle) {
        Multicast.port = port;
        Multicast.group = group;
        Multicast.socket = socket;
        new Send("ola");
        new Receive(controle).start();
    }

    public String getMensagem() {
        return mensagem;
    }

    private void setMensagem(String msg) {
        mensagem = msg;
    }

    public static class Send {
        Send(String msg) {
            try {
                byte[] m = msg.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, port);
                socket.send(messageOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Receive extends Thread {
        String msg = "";
        private Controle controle;

        Receive(Controle controle) {
            this.controle = controle;
        }

        @Override
        public void run() {
            while (!msg.equals("exit")) {
                byte[] buffer = new byte[1000];
                //for (int i = 0; i < 3; i++) {        // get messages from others in group
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(messageIn);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                msg = new String(messageIn.getData()).trim();
                setMensagem(msg);

                if (msg.equals("ola")) {
                    controle.start();
                }
            }
        }
    }
}
