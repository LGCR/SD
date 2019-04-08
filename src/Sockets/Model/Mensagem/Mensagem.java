package Sockets.Model.Mensagem;

import java.io.Serializable;
import java.security.PublicKey;

public class Mensagem implements Serializable {

    private Long diferencaTempo;
    private PublicKey chavePublica;

    public Mensagem(Long diferencaTempo) {
        this.diferencaTempo = diferencaTempo;
    }

    public Mensagem(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public PublicKey getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public Long getDiferencaTempo() {
        return diferencaTempo;
    }

    public void setDiferencaTempo(Long diferencaTempo) {
        this.diferencaTempo = diferencaTempo;
    }
}
