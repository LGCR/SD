package Sockets.Controller;

import Sockets.Controller.Disparadores.CorrigeTempo;
import Sockets.Controller.Disparadores.DisponibilizaMestre;
import Sockets.Controller.Disparadores.VerificaMestre;
import Sockets.Model.Mensagem.Mensagem;
import Sockets.Model.PacoteMensagem;
import Sockets.Model.Processo;
import Sockets.Model.ProcessoDAO;
import Sockets.Util.EncriptaDecripta;
import Sockets.View.Tela;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyPair;
import java.security.SignatureException;
import java.util.Timer;

public class Controle {
    public final ControleMulticast controleMulticast;
    public final ControleUnicast controleUnicast;
    public final RelogioVirtual relogioVirtual;
    public final Tela tela;
    public final ProcessoDAO processos;
    private final VerificaMestre verificaMestre;
    private final DisponibilizaMestre dispobibilzaMestre;
    private final CorrigeTempo corretorTempo;


    //Esta é o contrutor da principal classe do processo, nele será intanciada e ativada todas as threads de processo
    public Controle() {

        //Essa linha instancia o disparador de mensagens do pronpt
        this.tela = new Tela(this);

        //Instancia do relogio digital
        relogioVirtual = new RelogioVirtual(true);
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

        Long deltaTempo = 1000L;
        this.corretorTempo = new CorrigeTempo(this, deltaTempo);
        this.tela.adicionarLog("Instanciando controlador de tempo dos escravos");

        //Inicia disponibilizador do mestre
        this.dispobibilzaMestre = new DisponibilizaMestre(this, deltaTempo);

        //Iniciando verificador do mestre
        this.verificaMestre = new VerificaMestre(this, deltaTempo);

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
                                    Mensagem.converteMensagemParaArrayBytes(
                                            new Mensagem(
                                                    this.processos.getEsteProcesso().getChavePublica()
                                            )
                                    )
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
                if (this.processos.getMestre() != null && this.processos.getMestre().getIdentificador().equals(mensagem.getIdRemetente())) {
                    this.verificaMestre.mestreDisponivel();
                } else if (this.processos.getMestre() == null) {
                    if (!this.processos.idExistente(mensagem.getIdRemetente())) {
                        this.adicionarProcessoPacoteMensagem(mensagem);
                    }
                    this.processos.setMestre(mensagem.getIdRemetente());
                } else {
                    this.tela.adicionarLog("Ignorando falso mestre: " + mensagem.getIdRemetente());
                }

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.REQUISICAO_TEMPO) {
                if (mensagem.getIdRemetente().equals(this.processos.getMestre().getIdentificador())) {
                    try {

                        this.controleUnicast.enviarMensagem(
                                new PacoteMensagem(
                                        this.processos.getEsteProcesso().getIdentificador(),
                                        PacoteMensagem.RESPOSTA_TEMPO,
                                        Mensagem.converteMensagemParaArrayBytes(
                                                new Mensagem(
                                                        this.relogioVirtual.getTempo()
                                                )
                                        )
                                ),
                                this.processos.getMestre().getEndereco(), this.processos.getMestre().getPorta()
                        );

                        this.tela.adicionarLog("Enviando tempo para o mestre");
                        return;

                    } catch (IOException | SignatureException e) {
                        this.tela.adicionarLog("Falha ao enviar tempo para o mestre");
                        e.printStackTrace();
                    }
                    this.tela.adicionarLog("Falha ao enviar tempo para o mestre");
                } else {
                    this.tela.adicionarLog("Ignorando falso mestre: " + mensagem.getIdRemetente());
                }
            } else if (mensagem.getTipoMensagem() == PacoteMensagem.RESPOSTA_TEMPO && this.corretorTempo.getRecebeTempo()) {
                if (this.processos.idExistente(mensagem.getIdRemetente())) {
                    int index = this.processos.getIndexPorID(mensagem.getIdRemetente());
                    this.processos.setMomentoChegada(index, this.relogioVirtual.getTempo());
                    try {
                        this.processos.setTempo(index, Mensagem.converteArrayBytesParaMensagem(mensagem.getMensagem()).getTempo());
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    this.tela.adicionarLog("Recebendo tempo de " + mensagem.getIdRemetente());

                } else {
                    this.tela.adicionarLog("Resposta de escravo desconhecido: " + mensagem.getIdRemetente());
                }

            } else if (mensagem.getTipoMensagem() == PacoteMensagem.AJUSTE_TEMPO && this.processos.getMestre() != null && this.processos.getMestre().getIdentificador().equals(mensagem.getIdRemetente())) {
                try {
                    this.relogioVirtual.somarTempo(Mensagem.converteArrayBytesParaMensagem(mensagem.getMensagem()).getTempo());
                    this.tela.adicionarLog("Ajustando relogio em " + Mensagem.converteArrayBytesParaMensagem(mensagem.getMensagem()).getTempo() + " segundos");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

            } else {
                this.tela.adicionarLog("Chegou uma mensagem não tratavel de " + mensagem.getIdRemetente());
            }

        } else {
            if (mensagem.getTipoMensagem() != PacoteMensagem.DISPONIVEL)
                this.tela.adicionarLog("Ignorando mensagem de " + mensagem.getIdRemetente());
        }

    }

    //Adicionar um novo processo a partir de uma mensagem
    private void adicionarProcessoPacoteMensagem(PacoteMensagem mensagem) {
        try {
            if (this.processos.adicionarProcesso
                    (new Processo(
                                    mensagem.getIdRemetente(),
                                    InetAddress.getByName(mensagem.getIdRemetente().split("/")[0]),
                                    Integer.parseInt(mensagem.getIdRemetente().split("/")[1]),
                                    Mensagem.converteArrayBytesParaMensagem(mensagem.getMensagem()).getChavePublica(),
                                    false
                            )
                    )
            ) {
                this.tela.adicionarLog("Novo processo de ID: " + mensagem.getIdRemetente());
            } else {
                this.tela.adicionarLog("Processo " + mensagem.getIdRemetente() + " já existe");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    //Esta função torna o próprio processo um mestre
    public void virarMestre() {
        this.dispobibilzaMestre.disponibilizarMestre();
        this.tela.adicionarLog("Iniciando disparador de mestre disponivel");
        this.corretorTempo.iniciarCorretor();
        this.tela.adicionarLog("Iniciando controlador de escravos");

    }


}
