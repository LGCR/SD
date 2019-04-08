package Sockets.Model;

import org.jetbrains.annotations.Contract;
import org.json.JSONObject;

public class PacoteMensagem {

    //Essa clase contém a definição da estrutura do pacote de mensagens que será trocada pelos processos
    private String idRemetente;
    private byte tipoMensagem;
    private Object mensagem;


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


    public PacoteMensagem(String idRemetente, byte tipoMensagem, Object mensagem) {
        this.idRemetente = idRemetente;
        this.tipoMensagem = tipoMensagem;
        this.mensagem = mensagem;
    }

    public Object getMensagem() {
        return mensagem;
    }

    public void setMensagem(Object mensagem) {
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
}
