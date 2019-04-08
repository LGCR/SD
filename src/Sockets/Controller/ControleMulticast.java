package Sockets.Controller;

import Sockets.Util.ConversorJSON;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ControleMulticast extends Thread {

    //Essa variável armazena o IP controleMulticast que o processo usará para se conectar
    private static final String ip = "228.5.6.7";

    private final Controle controle;

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
            this.multicastSocket.send(new DatagramPacket(mensagem, mensagem.length, InetAddress.getByName(
                    ip), this.porta));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem para grupo controleMulticast");
        }
    }

    //Essa será a função de thread e será o Listener de mensagens controleMulticast
    @Override
    public void run() {

        //garante que a thread não morra
        while (true) {

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

            //Chama a função de tratamento de mensagem na controler
            this.controle.tratadorMensagens(
                    //converte um objeto JSON para PacoteMensagem
                    ConversorJSON.converteJsonParaModelo(
                            //Cria uma String a partir de um array de bytes
                            new JSONObject(
                                    //Retorna o array de bytes recebido
                                    new String(mensagemRecebida.getData())
                            )
                    )
            );
        }
    }
}