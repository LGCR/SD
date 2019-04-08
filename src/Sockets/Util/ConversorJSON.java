package Sockets.Util;

import Sockets.Model.PacoteMensagem;
import org.json.JSONObject;

public class ConversorJSON {


    //Essa função converte o modelo de mensagem para um string JSON
    public static String converteModeloParaJson(PacoteMensagem mensagem){
        JSONObject mensagemJSON = new JSONObject();

        mensagemJSON.put("idRemetente", mensagem.getIdRemetente());
        mensagemJSON.put("tipoMensagem", (int) mensagem.getTipoMensagem());
        mensagemJSON.put("mensagem", mensagem.getMensagem());

        return mensagemJSON.toString();
    }

    public static PacoteMensagem converteJsonParaModelo(JSONObject mensagemJSON){
        PacoteMensagem retorno =  new PacoteMensagem(mensagemJSON.getString("idRemetente"),(byte) mensagemJSON.getInt("tipoMensagem"), null);

        if(PacoteMensagem.ENTRADA == retorno.getTipoMensagem() || retorno.getTipoMensagem() == PacoteMensagem.EXISTENCIA){
          //retorno.setMensagem();
        }


        return retorno;
    }


}
