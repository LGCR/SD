package Sockets.Model;

import java.net.InetAddress;
import java.security.PublicKey;

public class Processo implements Comparable {
    private String identificador;
    private InetAddress endereco;
    private int porta;
    private PublicKey chavePublica;
    private Boolean master;
    private Long segundos;

    public Processo(String identificador, InetAddress endereco, int porta, PublicKey chavePublica, Boolean master) {
        this.identificador = identificador;
        this.endereco = endereco;
        this.porta = porta;
        this.chavePublica = chavePublica;
        this.master = master;
        this.segundos = null;
    }

    public Long getSegundos() {
        return segundos;
    }

    public void setSegundos(Long segundos) {
        this.segundos = segundos;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public InetAddress getEndereco() {
        return endereco;
    }

    public void setEndereco(InetAddress endereco) {
        this.endereco = endereco;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public PublicKey getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    @Override
    public int compareTo(Object o) {
        Processo compara = (Processo) o;
        return this.getIdentificador().compareTo(compara.getIdentificador());
    }
}
