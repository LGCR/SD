package Sockets.Model;

public class Processo {
    private String identificador;
    private String ip;
    private int porta;
    private String publicKey;
    private Boolean master;

    public Processo(String identificador, String ip, int porta, String publicKey, Boolean master) {
        this.identificador = identificador;
        this.ip = ip;
        this.porta = porta;
        this.publicKey = publicKey;
        this.master = master;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
}
