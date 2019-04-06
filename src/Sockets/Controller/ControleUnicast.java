package Sockets.Controller;

import java.io.IOException;
import java.net.*;

public class ControleUnicast implements Runnable {

    private DatagramSocket unicastSocket;

    private Controle controle;

    public ControleUnicast(Controle controle) {

        //Aqui é feito a injeção de dependencia da classe controle
        this.controle = controle;

        //O sistema operacional irá definir um porta livre para o socket
        try {
            this.unicastSocket = new DatagramSocket(0,InetAddress.getByName("localhost"));
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Falha ao iniciar soket unicast! Finalizando processo");
            System.exit(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    //retorna a porta sendo utilizada
    public int getPorta(){
        return this.unicastSocket.getLocalPort();
    }

    //retorna o IP sendo utilizado para a comunicação unicast
    public InetAddress getIP(){
        return this.unicastSocket.getLocalAddress();
    }

    public synchronized void enviarMensagem(byte[] mensagem, InetAddress endereco, int porta){

        //Essa fução encapsula a mensagem e envia para a porta e endereço passado como parâmetro
        try {
            this.unicastSocket.send(new DatagramPacket(mensagem, mensagem.length,endereco, porta));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem");
        }
    }

    @Override
    public void run() {

        //instancia o datagrama com 0.5MB de buffer
        DatagramPacket mensagemRecebida = new DatagramPacket(new byte[5120],5120);

        //Litener bloqueante de leitura do socket
        try {
            this.unicastSocket.receive(mensagemRecebida);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //reinicia uma nova thread visto que essa irá executar o call e stack e morrer
        this.run();

    }
}