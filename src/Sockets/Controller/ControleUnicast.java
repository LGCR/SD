package Sockets.Controller;

import Sockets.Model.PacoteMensagem;
import Sockets.Util.EncriptaDecripta;

import java.io.IOException;
import java.net.*;
import java.security.SignatureException;

public class ControleUnicast extends Thread {

    private DatagramSocket unicastSocket;

    private final Controle controle;

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

    public synchronized void enviarMensagem(PacoteMensagem pacote, InetAddress endereco, int porta) throws IOException, SignatureException {

        //Adiciona a assinatura no pacote
        pacote.setAssinaturaMensagem(
                EncriptaDecripta.assina(
                        pacote.getMensagem(),
                        this.controle.processos.getChavePrivada()
                ).sign()
        );

        //transoforma o pacote em array de bytes
        byte[] mensagem = PacoteMensagem.convertePacoteMensagemParaArrayBytes(pacote);


        //Essa fução encapsula a mensagem e envia para a porta e endereço passado como parâmetro
        this.unicastSocket.send(new DatagramPacket(mensagem, mensagem.length, endereco, porta));
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {

        //garante que a threa não morra
        while (true) {

            //instancia o datagrama com 0.5MB de buffer
            DatagramPacket mensagemRecebida = new DatagramPacket(new byte[2048], 2048);

            //Litener bloqueante de leitura do socket
            try {
                this.unicastSocket.receive(mensagemRecebida);

                PacoteMensagem mensagem = PacoteMensagem.converteArrayBytesParaPacoteMensagem(mensagemRecebida.getData());

                if (
                        EncriptaDecripta.verificaAssinatura(
                                mensagem.getMensagem(),
                                this.controle.processos.getProcessoEspecifico(
                                        this.controle.processos.getIndexPorID(
                                                mensagem.getIdRemetente()
                                        )
                                ).getChavePublica(),
                                mensagem.getAssinaturaMensagem()
                        )
                ) {
                    this.controle.tratadorMensagens(mensagem);
                } else {
                    this.controle.tela.adicionarLog("Assinatura de " + mensagem.getIdRemetente() + " não correspondente!");
                }
                continue;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.controle.tela.adicionarLog("Erro ao tratar nova mensagem unicast");
        }
    }
}