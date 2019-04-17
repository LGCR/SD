package Sockets.Model;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class PacoteMensagem implements Serializable {

    //Essa clase contém a definição da estrutura do pacote de mensagens que será trocada pelos processos
    private String idRemetente;
    private byte tipoMensagem;
    private byte[] mensagem;
    private byte[] assinaturaMensagem;


    //definição dos tipos de mensagem possíveis

    //Esse tipo de mensagem é utilizada quando um processo entra no grupo multicast
    public static final byte ENTRADA = 0;

    //Esse tipo de mensagem serve para responder o processo que entrou com os dados dos processos já existentes
    public static final byte EXISTENCIA = 1;

    //Esse tipo de mensagem não apresenta conteudo e serve apenas para identificar que o Mestre esta vivo
    public static final byte DISPONIVEL = 2;

    //Esse tipo de mensagem não apresenta conteudo e serve para que os processos escravos enviem seu tempo de relogio
    public static final byte REQUISICAO_TEMPO = 3;

    //Esse tipo de mensagem enviará o tempo do relogio para o mestre
    public static final byte RESPOSTA_TEMPO = 4;

    //Esse tipo de mensagem conterá o quanto de tempo deve ser ajustado no relogio
    public static final byte AJUSTE_TEMPO = 5;


    public PacoteMensagem(String idRemetente, byte tipoMensagem,@NotNull byte[] mensagem) {
        this.idRemetente = idRemetente;
        this.tipoMensagem = tipoMensagem;
        this.mensagem = mensagem;
        this.assinaturaMensagem = null;
    }

    public PacoteMensagem(String idRemetente, byte tipoMensagem,@NotNull byte[] mensagem, byte[] assinaturaMensagem) {
        this.idRemetente = idRemetente;
        this.tipoMensagem = tipoMensagem;
        this.mensagem = mensagem;
        this.assinaturaMensagem = assinaturaMensagem;
    }

    public byte[] getAssinaturaMensagem() {
        return assinaturaMensagem;
    }

    public void setAssinaturaMensagem(byte[] assinaturaMensagem) {
        this.assinaturaMensagem = assinaturaMensagem;
    }

    public byte[] getMensagem() {
        return this.mensagem;
    }

    public void setMensagem(byte[] mensagem) {
        this.mensagem = mensagem;
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public int getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(byte tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public static PacoteMensagem converteArrayBytesParaPacoteMensagem(byte[] array) throws IOException, ClassNotFoundException {
        ByteArrayInputStream input = new ByteArrayInputStream(array);
        ObjectInputStream objectInput = new ObjectInputStream(input);
        return  (PacoteMensagem) objectInput.readObject();
    }

    public static byte[] convertePacoteMensagemParaArrayBytes( PacoteMensagem pacote) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(output);
        objectOutput.writeObject(pacote);
        return output.toByteArray();
    }

}
