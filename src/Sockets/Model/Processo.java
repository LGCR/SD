package Sockets.Model;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class Processo implements Comparable{
    private String identificador;
    private InetAddress endereco;
    private int porta;
    private String publicKey;
    private Boolean master;
    private Long timeLastContact;

    public Processo(String identificador, InetAddress endereco, int porta, String publicKey, Boolean master, Long timeLastContact) {
        this.identificador = identificador;
        this.endereco = endereco;
        this.porta = porta;
        this.publicKey = publicKey;
        this.master = master;
        this.timeLastContact = timeLastContact;
    }

    public Long getTimeLastContact() {
        return timeLastContact;
    }

    public void setTimeLastContact(Long timeLastContact) {
        this.timeLastContact = timeLastContact;
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
