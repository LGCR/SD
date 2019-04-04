package Sockets.Controller;

import Sockets.MasterAtivo;
import Sockets.Model.Processo;
import Sockets.VerificaMaster;
import Sockets.View.Grid;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.Timer;
import java.util.UUID;

public class Controle extends Thread {
    public String id;
    public ControleMulticast controleMulticast;
    public VerificaMaster verificaMaster;
    PrivateKey privateKey;

    DatagramSocket unisocket;

    int port = 6789;
    Grid grid;

    RelogioDigital relogioDigital;
    Processo processo;
    MasterAtivo masterAtivo;

    //Esta é o contrutor da principal classe do processo, nele será intanciada e ativada todas as threads de processo
    public Controle() throws IOException {

        //Instancia do relogio digital
        this.relogioDigital = new RelogioDigital(false, 100L);

        //Aqui será instanciado a classe de controle ControleMulticast
        this.controleMulticast = new ControleMulticast(this);


        geraID();
        verificaMaster();
        criaProcesso();
        imprimeGrid();

        //iniciando Threads

        //iniciamdo thread do relógio digital
        (new Timer()).schedule(this.relogioDigital, this.relogioDigital.getTaxaAtualizacao(), this.relogioDigital.getTaxaAtualizacao());

        //Iniciando listener multisoket
        this.controleMulticast.run();
    }

    public void masterAtivo() {
        Timer timer = new Timer();
        masterAtivo = new MasterAtivo(id, this);
        timer.schedule(masterAtivo, 1000, 1000);
    }

    private void criaProcesso() {
        processo = new Processo(id, verificaMaster.master.getMaster());
    }

    private void verificaMaster() {
        Timer timer = new Timer();
        verificaMaster = new VerificaMaster(this, id);
        timer.schedule(verificaMaster, 5000, 5000);
    }

    private void imprimeGrid() {
        grid = new Grid(this);
        Timer gridTimer = new Timer();
        gridTimer.schedule(grid, 1000, 5000);
    }

    private void geraID() throws SocketException {
        UUID uuid = UUID.randomUUID();
        unisocket = new DatagramSocket();
        id = uuid.toString().substring(0, 4).concat(":".concat(Integer.toString(unisocket.getLocalPort())));
        System.out.println(id);
    }

    @Override
    public void run() {

        while (!(this.controleMulticast.ultimaMensagem).equals("exit")) {

            if ((this.controleMulticast.ultimaMensagem).equals("ola")) {
                masterAtivo();
            }
        }
    }
}
