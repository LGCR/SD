package Sockets.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ControleMulticast implements Runnable {

    //Essa variável armazena o IP controleMulticast que o processo usará para se conectar
    private static final String ip = "228.5.6.7";
    private final Controle controle;
    public java.lang.String ultimaMensagem = "";
    //Essa variável armazena a porta onde ficará conectado o soket multicast
    private int porta;
    //Essa variável armazena o objeto que irá lidar com ControleMulticast
    private MulticastSocket multicastSocket;

    public ControleMulticast(Controle controle) {

        //Injeção de dependencia
        this.controle = controle;

        //Aqui será instanciado o objeto do tipo InetAdrres através de um IP pré selecionado
        InetAddress grupo = null;
        try {
            grupo = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Falha ao encontrar endereço de IP");
            System.exit(1);
        }

        //Aqui será instanciado o objeto para lidar com MultiCastSocket. A porta será definida pelo SO
        try {

            //Esse comando retorna uma porta do SO que esteja disponível para o uso
            //this.porta = (new ServerSocket(0)).getLocalPort();

            //Estabelece a porta de conexão multicast
            this.porta = 6789;

            this.multicastSocket = new MulticastSocket(this.porta);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao abrir tratador multicast");
            System.exit(1);
        }

        //Aqui o objeto controleMulticast irá entrar na sala do IP pré estabelecido
        try {
            this.multicastSocket.joinGroup(grupo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Aqui será enviada uma mensagem de 'olá' para todo o grupo controleMulticast
        this.enviarMensagem(("ola").getBytes());

        /*
          A thread de recebimento deve ser iniciada na classe Controle para manter a orientação a objeto
         */
    }

    //Essa função retorna o IP ultilizado na comunicação multicast
    public String getIPMulticast() {
        return ip;
    }

    //Essa função retorna a porta atualmente ultilizada
    public int getPorta() {
        return this.porta;
    }

    //Essa função irá enviar uma mensagem para o grupo controleMulticast
    public synchronized void enviarMensagem(byte[] mensagem) {

        try {
            this.multicastSocket.send(new DatagramPacket(mensagem, mensagem.length, this.multicastSocket.getInterface(), this.porta));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem para grupo controleMulticast");
        }
    }

    //Essa será a função de thread e será o Listener de mensagens controleMulticast
    @Override
    public void run() {


        //Aqui será instanciado o datagrama para recebimento de mensagens multicasta com 0.5 MB de buffer
        DatagramPacket mensagemRecebida = new DatagramPacket(new byte[500000], 500000);

        //Aquui está o listener bloqueante que escutara o socket controleMulticast
        try {
            this.multicastSocket.receive(mensagemRecebida);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha ao receber mensagem controleMulticast! Reiniciando a Thread");
            return;
        }

        //reinicia uma nova thread visto que essa irá executar o call e stack e morrer
        this.run();

            /*
              Aqui deverá ser chamada alguma função de controle da classe controle para tratar o array de byte recebido.
              Para manter a integridade do software até o momento será escrito um código provisorio, para que o mesmo
              continue funcionando sem complicações.
              O CÓDIGO ABAIXO DEVE SER APAGADO NO FUTURO
             */

        System.out.println((new String(mensagemRecebida.getData()).trim()));
        if (((new String(mensagemRecebida.getData()).trim())).equals("ola")) {
            this.ultimaMensagem = (Arrays.toString(mensagemRecebida.getData())).trim();
            System.out.println(this.ultimaMensagem);
            if (!this.controle.isAlive())
                this.controle.start();
        }


    }

}
/*
public static class Send {
    Send(String msg) {
        try {
            byte[] m = msg.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, grupo, porta);
            multicastSocket.send(messageOut);
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
            //for (int i = 0; i < 3; i++) {        // get messages from others in grupo
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            try {
                multicastSocket.receive(messageIn);
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
}*/
