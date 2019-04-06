package Sockets.Model;

import org.jetbrains.annotations.Contract;
import org.json.JSONObject;

public class Mensagem {

    //Essa clase contém a definição da estrutura do pacote de mensagens que será trocada pelos processos
    private String idRemetente;
    private byte tipoMensagem;
    private Object mensagem;


    //definição dos tipos de mensagem possíveis

    //esse tipo de mensagem é destinado a quando um processo entra na rede multicast
    public static final byte BOAS_VINDAS = 0;

    //esse tipo de mensagem serve para que os processos troquem informações basicas que identifiquem que os mesmo estão vivos
    public static final byte EXISTENCIA = 1;


    public Mensagem(String idRemetente, byte tipoMensagem, Object mensagem) {
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
