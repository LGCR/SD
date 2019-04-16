package Sockets.Controller;

import Sockets.Model.PacoteMensagem;
import Sockets.Model.Processo;
import Sockets.Util.EncriptaDecripta;

import java.io.IOException;
import java.net.*;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class ControleUnicast extends Thread {

    private DatagramSocket unicastSocket;

    private Controle controle;

    public ControleUnicast(Controle controle) {

        //Aqui é feito a injeção de dependencia da classe controle
        this.controle = controle;

        //O sistema operacional irá definir um porta livre para o socket
        try {
            this.unicastSocket = new DatagramSocket(0, InetAddress.getByName("localhost"));
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Falha ao iniciar soket unicast! Finalizando processo");
            System.exit(1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    //retorna a porta sendo utilizada
    public int getPorta() {
        return this.unicastSocket.getLocalPort();
    }

    //retorna o IP sendo utilizado para a comunicação unicast
    public InetAddress getIP() {
        return this.unicastSocket.getLocalAddress();
    }

    public synchronized void enviarMensagem(byte[] mensagem, InetAddress endereco, int porta) {

        //Essa fução encapsula a mensagem e envia para a porta e endereço passado como parâmetro
        try {
            Signature signature = EncriptaDecripta.assina(mensagem, controle.processos.getChavePrivada());
            byte []sig = signature.sign();
            this.unicastSocket.send(new DatagramPacket(sig, sig.length, endereco, porta));
        } catch (IOException | SignatureException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem");
        }
    }

    @Override
    public void run() {

        //garante que a threa não morra
        while (true) {

            //instancia o datagrama com 0.5MB de buffer
            DatagramPacket mensagemRecebida = new DatagramPacket(new byte[5120], 5120);

            //Litener bloqueante de leitura do socket
            try {
                this.unicastSocket.receive(mensagemRecebida);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                byte []sig = mensagemRecebida.getData();
                PublicKey publicKey = null;
                for (int i = 0; i < controle.processos.getNumeroProcessos(); i++){
                    Processo processo = controle.processos.getProcessoEspecifico(i);
                    if (EncriptaDecripta.verificaAssinatura(sig, processo.getChavePublica())){
                        publicKey = processo.getChavePublica();
                    }
                }

                if (EncriptaDecripta.verificaAssinatura(sig, publicKey)){

                    this.controle.tratadorMensagens(
                            PacoteMensagem.converteArrayBytesParaPacoteMensagem(

                                    sig


                            )
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.controle.tela.adicionarLog("Erro ao tratar nova mensagem unicast");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                this.controle.tela.adicionarLog("Erro ao tratar nova mensagem unicast");
            }


        }
    }
}