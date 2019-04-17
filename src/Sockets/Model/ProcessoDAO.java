package Sockets.Model;

import java.security.PrivateKey;
import java.util.ArrayList;

public class ProcessoDAO {

    //Essa variável guarda a lista de processos descobertos até o momento
    private final ArrayList<Processo> listaProcessos;

    //Essa variável guarda a auto imagem deste processo
    private final Processo esteProcesso;

    private final PrivateKey chavePrivada;

    public ProcessoDAO(Processo processo, PrivateKey chave) {
        this.listaProcessos = new ArrayList<>();
        this.esteProcesso = processo;
        this.chavePrivada = chave;
    }

    public PrivateKey getChavePrivada() {
        return chavePrivada;
    }

    public int getNumeroProcessos() {
        return this.listaProcessos.size();
    }

    public Processo getProcessoEspecifico(int index) {
        if (index < this.getNumeroProcessos()) {
            return this.listaProcessos.get(index);
        } else {
            return null;
        }
    }

    public void setTempo(int index, Long segundos) {
        if (index >= this.getNumeroProcessos())
            return;
        this.listaProcessos.get(index).setTempo(segundos);
    }

    public void setMomentoEnvio(int index, Long segundos) {
        if (index >= this.getNumeroProcessos())
            return;
        this.listaProcessos.get(index).setMomentoEnvio(segundos);
    }

    public void setMomentoChegada(int index, Long segundos) {
        if (index >= this.getNumeroProcessos())
            return;
        this.listaProcessos.get(index).setMomentoChegada(segundos);
    }

    public int getIndexPorID(String id) {
        for (int contador = 0; contador < this.getNumeroProcessos(); contador++)
            if (this.listaProcessos.get(contador).getIdentificador().equals(id))
                return contador;

        return -1;
    }

    public Processo getMestre() {
        if (this.esteProcesso.getMaster()) {
            return esteProcesso;
        } else {
            if (this.listaProcessos.size() == 0)
                return null;
            for (Processo listaProcesso : this.listaProcessos) {
                if (listaProcesso.getMaster())
                    return listaProcesso;
            }
            return null;
        }
    }

    @SuppressWarnings("SuspiciousListRemoveInLoop")
    public void novoMestre() {
        for (int contador = 0; contador < this.listaProcessos.size(); contador++) {
            if (this.listaProcessos.get(contador).getMaster()) {
                this.listaProcessos.remove(contador);
            }
        }

        if (this.listaProcessos.size() == 0 || esteProcesso.compareTo(this.listaProcessos.get(0)) < 0) {
            this.esteProcesso.setMaster(true);
        } else {
            this.listaProcessos.get(0).setMaster(true);
        }
    }

    public Boolean adicionarProcesso(Processo processo) {
        if (this.idExistente(processo.getIdentificador())) {
            if (this.esteProcesso.getIdentificador().equals(processo.getIdentificador()) &&
                    this.esteProcesso.getChavePublica() != processo.getChavePublica()) {
                this.esteProcesso.setChavePublica(processo.getChavePublica());
                return false;
            }
            for (Processo listaProcesso : this.listaProcessos) {
                if (listaProcesso.getIdentificador().equals(processo.getIdentificador()) &&
                        listaProcesso.getChavePublica() != processo.getChavePublica()
                ) {
                    listaProcesso.setChavePublica(processo.getChavePublica());
                    return false;
                }
            }
        }
        this.listaProcessos.add(processo);
        this.listaProcessos.sort(Processo::compareTo);
        return true;
    }

    public Processo getEsteProcesso() {
        return this.esteProcesso;
    }

    public void setMestre(String idMestre) {
        if (this.esteProcesso.getIdentificador().equals(idMestre))
            this.esteProcesso.setMaster(true);
        else {
            this.esteProcesso.setMaster(false);
            for (Processo listaProcesso : this.listaProcessos) {
                if (listaProcesso.getIdentificador().equals(idMestre))
                    listaProcesso.setMaster(true);
                else
                    listaProcesso.setMaster(false);
            }
        }
    }

    public Boolean idExistente(String id) {
        if (this.esteProcesso.getIdentificador().equals(id))
            return true;
        for (Processo listaProcesso : this.listaProcessos)
            if (listaProcesso.getIdentificador().equals(id))
                return true;
        return false;
    }

}
