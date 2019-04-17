package Sockets.Controller.Disparadores;

import Sockets.Controller.Controle;
import Sockets.Model.Mensagem.Mensagem;
import Sockets.Model.PacoteMensagem;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Timer;
import java.util.TimerTask;

public class CorrigeTempo extends TimerTask {

    private Controle controle;

    private Long deltaTempo;

    private Timer disparador;

    private Boolean recebeTempo;


    public CorrigeTempo(Controle controle, Long deltaTempo) {
        this.controle = controle;
        this.deltaTempo = deltaTempo;
        this.disparador = new Timer();
        this.recebeTempo = false;
    }


    public void iniciarCorretor() {
        this.disparador.schedule(this, this.deltaTempo * 2, this.deltaTempo * 2);
    }

    public Boolean getRecebeTempo() {
        return recebeTempo;
    }

    @Override
    public void run() {
        //Quando o mestre tiver recebido os respectivos tempos, o mesmo deve parar de recebelos, realizar o acerto de horários e enviar para cada Processo
        if (this.recebeTempo) {
            this.recebeTempo = false;

            Long numeroEscravos = 0L;
            Long somaSegundos = 0L;

            for (int contador = 0; contador < this.controle.processos.getNumeroProcessos(); contador++) {
                if (this.controle.processos.getProcessoEspecifico(contador).getMomentoChegada() != null &&
                        this.controle.processos.getProcessoEspecifico(contador).getMomentoEnvio() != null &&
                        this.controle.processos.getProcessoEspecifico(contador).getTempo() != null) {
                    numeroEscravos++;
                    somaSegundos += this.controle.processos.getProcessoEspecifico(contador).getTempo() + ((this.controle.processos.getProcessoEspecifico(contador).getMomentoChegada() - this.controle.processos.getProcessoEspecifico(contador).getMomentoEnvio()) / 2);

                }
            }

            numeroEscravos++;
            somaSegundos += this.controle.relogioVirtual.getTempo();

            Long mediaTempo = somaSegundos / numeroEscravos;


            //Ajustando o próprio mestre
            this.controle.relogioVirtual.somarTempo((this.controle.relogioVirtual.getTempo() - mediaTempo) * -1L);
            this.controle.tela.adicionarLog("Auto ajuste de " + (this.controle.relogioVirtual.getTempo() - mediaTempo) + " segundos");

            for (int contador = 0; contador < this.controle.processos.getNumeroProcessos(); contador++) {
                if (this.controle.processos.getProcessoEspecifico(contador).getMomentoChegada() != null &&
                        this.controle.processos.getProcessoEspecifico(contador).getMomentoEnvio() != null &&
                        this.controle.processos.getProcessoEspecifico(contador).getTempo() != null) {

                    try {

                        this.controle.controleUnicast.enviarMensagem(
                                new PacoteMensagem(
                                        this.controle.processos.getEsteProcesso().getIdentificador(),
                                        PacoteMensagem.AJUSTE_TEMPO,
                                        new Mensagem(
                                                (
                                                        (this.controle.processos.getProcessoEspecifico(contador).getTempo() +
                                                                (
                                                                        this.controle.processos.getProcessoEspecifico(contador).getMomentoChegada() - this.controle.processos.getProcessoEspecifico(contador).getMomentoEnvio()
                                                                ) / 2
                                                        ) - mediaTempo
                                                ) * -1L
                                        )
                                ),
                                this.controle.processos.getProcessoEspecifico(contador).getEndereco(),
                                this.controle.processos.getProcessoEspecifico(contador).getPorta()
                        );

                        this.controle.tela.adicionarLog("Enviando ajuste de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
                        continue;

                    } catch (IOException | SignatureException e) {
                        this.controle.tela.adicionarLog("Falha ao enviar ajuste de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
                        e.printStackTrace();
                    }
                    this.controle.tela.adicionarLog("Falha ao enviar ajuste de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
                }
            }
        }
        //Quando o mestre não tiver recebendo tempo, ele deve zerar todas as variáveis relacionadas a estas medidas e mandar a requisição para seus escravos
        else {

            for (int contador = 0; contador < this.controle.processos.getNumeroProcessos(); contador++) {
                this.controle.processos.setMomentoChegada(contador, null);
                this.controle.processos.setMomentoEnvio(contador, null);
                this.controle.processos.setTempo(contador, null);
            }

            //Agora será enviado uma mensagem para cada escravo via unicast
            this.recebeTempo = true;

            for (int contador = 0; contador < this.controle.processos.getNumeroProcessos(); contador++) {
                try {

                    this.controle.controleUnicast.enviarMensagem(
                            new PacoteMensagem(
                                    this.controle.processos.getEsteProcesso().getIdentificador(),
                                    PacoteMensagem.REQUISICAO_TEMPO,
                                    null
                            ),
                            this.controle.processos.getProcessoEspecifico(contador).getEndereco(),
                            this.controle.processos.getProcessoEspecifico(contador).getPorta()
                    );

                    this.controle.processos.setMomentoEnvio(contador, this.controle.relogioVirtual.getTempo());
                    this.controle.tela.adicionarLog("Enviando requisição de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
                    continue;

                } catch (IOException | SignatureException e) {
                    e.printStackTrace();
                    this.controle.tela.adicionarLog("Falha ao enviar requisição de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
                }
                this.controle.tela.adicionarLog("Falha ao enviar requisição de tempo para " + this.controle.processos.getProcessoEspecifico(contador).getIdentificador());
            }

        }

    }
}
