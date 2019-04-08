package Sockets.Model;

import java.security.PrivateKey;
import java.util.ArrayList;

public class ProcessoDAO {

    //Essa variável guarda a lista de processos descobertos até o momento
    private ArrayList<Processo> listaProcessos;

    //Essa variável guarda a auto imagem deste processo
    private Processo esteProcesso;

    private PrivateKey chavePrivada;

    //Define um marcador para adicionar um novo processo
    private static byte ADICIONAR = 0;

    //Mata o mestre existente e seleciona um novo mestre
    private static byte REMOVER_MESTRE = 1;

    //Retorna o proesso atualmente marcado como mestre
    private static byte RETORNA_MESTRE = 2;

    //Retorna uma copia da lista de processos
    private static byte COPIA_PROCESSOS = 3;

    public ProcessoDAO(Processo processo, PrivateKey chave) {
        this.listaProcessos = new ArrayList<>();
        this.esteProcesso = processo;
        this.chavePrivada = chave;
    }

    public int getNumeroProcessos() {
        return this.listaProcessos.size();
    }

    public Processo getMestre() {
        if (this.esteProcesso.getMaster()) {
            return esteProcesso;
        } else {
            Object retorno = this.alteraListaProcessos(ProcessoDAO.RETORNA_MESTRE, null);
            if (retorno == null)
                return null;
            return (Processo) retorno;
        }
    }

    public void novoMestre() {
        this.alteraListaProcessos(ProcessoDAO.REMOVER_MESTRE, null);
    }

    public void adicionarProcesso(Processo processo) {
        this.alteraListaProcessos(ProcessoDAO.ADICIONAR, processo);
    }

    public Processo getEsteProcesso() {
        return this.esteProcesso;
    }

    public ArrayList<Processo> getCopiaListaProcessos() {
        return (ArrayList<Processo>) this.alteraListaProcessos(ProcessoDAO.COPIA_PROCESSOS, null);
    }

    private synchronized Object alteraListaProcessos(byte comando, Object referencia) {

        if (comando == ProcessoDAO.RETORNA_MESTRE) {
            if (this.listaProcessos.size() == 0)
                return null;
            for (int contador = 0; contador <= this.listaProcessos.size(); contador++) {
                if (this.listaProcessos.get(contador).getMaster())
                    return this.listaProcessos.get(contador);
            }
            return null;
        } else if (comando == ProcessoDAO.ADICIONAR) {
            this.listaProcessos.add((Processo) referencia);
            this.listaProcessos.sort(Processo::compareTo);
        } else if (comando == ProcessoDAO.REMOVER_MESTRE) {
            for (int contador = 0; contador < this.listaProcessos.size(); contador++) {
                if (this.listaProcessos.get(contador).getMaster()) {
                    this.listaProcessos.remove(contador);
                    continue;
                }
            }

            if (this.listaProcessos.size() == 0 || esteProcesso.compareTo(this.listaProcessos.get(0)) > 0) {
                this.esteProcesso.setMaster(true);
            } else {
                this.listaProcessos.get(0).setMaster(true);
            }
        } else if (comando == ProcessoDAO.COPIA_PROCESSOS) {
            ArrayList<Processo> retorno = new ArrayList<>();
            for (int contador = 0; contador < this.listaProcessos.size(); contador++)
                retorno.add(this.listaProcessos.get(contador));
            return retorno;
        }
        return null;
    }
}
