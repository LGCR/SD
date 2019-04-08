package Sockets.Controller;

import Sockets.Controller.Disparadores.VerificaMestre;
import Sockets.Model.Mensagem.Mensagem;
import Sockets.Model.PacoteMensagem;
import Sockets.Model.Processo;
import Sockets.Model.ProcessoDAO;
import Sockets.Util.EncriptaDecripta;
import Sockets.View.Tela;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.Timer;

public class Controle {
    public ControleMulticast controleMulticast;
    public ControleUnicast controleUnicast;
    public RelogioVirtual relogioVirtual;
    public Tela tela;
    public ProcessoDAO processos;
    public VerificaMestre verificaMestre;
    private final Long DeltaTempo = 500L;


    //Esta é o contrutor da principal classe do processo, nele será intanciada e ativada todas as threads de processo
    public Controle() {

        //Essa linha instancia o disparador de mensagens do pronpt
        this.tela = new Tela(this);

        //Instancia do relogio digital
        relogioVirtual = new RelogioVirtual(false);
        this.tela.adicionarLog("Instanciando relogio virtual");

        //Aqui será instanciado a classe de controle ControleMulticast
        this.controleMulticast = new ControleMulticast(this);
        this.tela.adicionarLog("Instanciando controlador de multicasting");

        //Aqui será instanciado a classe que lidará com a comunicação unicast
        this.controleUnicast = new ControleUnicast(this);
        this.tela.adicionarLog("Instanciando controlador unicast");


        //Gera um par de chaves
        KeyPair chaves = EncriptaDecripta.geraChave();

        this.processos = new ProcessoDAO(
                new Processo(
                        this.controleUnicast.getIP().getHostAddress() + "/" + this.controleUnicast.getPorta(),
                        this.controleUnicast.getIP(),
                        this.controleUnicast.getPorta(),
                        chaves.getPublic(),
                        false
                ), chaves.getPrivate()
        );
        this.tela.adicionarLog("Gerando chave privada");
        this.tela.adicionarLog("Criando auto imagem do processo");


        //iniciando Threads

        //Iniciando listener multisoket
        this.controleMulticast.start();
        this.tela.adicionarLog("Iniciando listener Multicast");

        //Iniciando o listener unicast
        this.controleUnicast.start();
        this.tela.adicionarLog("Iniciando Listener Unicast");

        //iniciando tela
        new Timer().schedule(this.tela, 1L, 1000L);

        //Iniciando verificador do mestre
        this.verificaMestre = new VerificaMestre(this, this.DeltaTempo);

        this.enviarMensagensReconhecimento(PacoteMensagem.ENTRADA);
        this.tela.adicionarLog("Enviando mensagem de entrada");
    }

    //Essa função é destinada a enviar mensagens do tipo ENTRADA e EXISTENCIA
    private void enviarMensagensReconhecimento(byte tipoMensagem) {

        //chama a função para o envio de uma mensagem via multicast
        try {
            this.controleMulticast.enviarMensagem(
                    //Converte o PacoteMensagem para um objeto JSON
                    PacoteMensagem.convertePacoteMensagemParaArrayBytes(
                            //Cria um PacoteMensagem
                            new PacoteMensagem(
                                    //ID do remetente
                                    this.processos.getEsteProcesso().getIdentificador(),
                                    //Tipo da mensagem
                                    tipoMensagem,
                                    //Envia a chave publica como mensagem
                                    new Mensagem(this.processos.getEsteProcesso().getChavePublica())
                            )
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
            this.tela.adicionarLog("Falha ao enviar mensagem multicast");
        }
    }

    public final synchronized void tratadorMensagens(PacoteMensagem mensagem) {
        if (!mensagem.getIdRemetente().equals(this.processos.getEsteProcesso().getIdentificador())) {

            //Lida com o caso de chegar uma mensagem do tipo ENTRADA
            if (mensagem.getTipoMensagem() == PacoteMensagem.ENTRADA) {
                this.adicionarProcessoPacoteMensagem(mensagem);
                this.enviarMensagensReconhecimento(PacoteMensagem.EXISTENCIA);
            }

            //Lida com o caso de chegar uma mensagem do tipo EXISTENCIA
            else if (mensagem.getTipoMensagem() == PacoteMensagem.EXISTENCIA) {
                this.adicionarProcessoPacoteMensagem(mensagem);

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.DISPONIVEL) {

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.REQUISICAO_TEMPO) {

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.RESPOSTA_TEMPO) {

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.AJUSTE_TEMPO) {

            }
            else{
                this.tela.adicionarLog("Chegou uma mensagem não tratavel de " + mensagem.getIdRemetente());
            }

        } else {
            this.tela.adicionarLog("Ignorando mensagem de " + mensagem.getIdRemetente());
        }

    }

    //Adicionar um novo processo a partir de uma mensagem
    private void adicionarProcessoPacoteMensagem(PacoteMensagem mensagem) {
        try {
            this.processos.adicionarProcesso(
                    new Processo(
                            mensagem.getIdRemetente(),
                            InetAddress.getByName(mensagem.getIdRemetente().split("/")[0]),
                            Integer.parseInt(mensagem.getIdRemetente().split("/")[1]),
                            mensagem.getMensagem().getChavePublica(),
                            false
                    )
            );
            this.tela.adicionarLog("Novo processo de ID: " + mensagem.getIdRemetente());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            this.tela.adicionarLog("Falha ao adicionar processo de id: " + mensagem.getIdRemetente());
        }


    }


}
