package Sockets.Controller;

import Sockets.MasterAtivo;
import Sockets.Model.Processo;
import Sockets.VerificaMaster;
import Sockets.View.Grid;
import Util.EncriptaDecripta;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.UUID;

public class Controle extends Thread {
    public String id;
    public final ControleMulticast controleMulticast;
    public VerificaMaster verificaMaster;
    public Processo processo;

    //Esta é o contrutor da principal classe do processo, nele será intanciada e ativada todas as threads de processo
    public Controle() throws IOException {

        //Aqui é instanciado a classe que irá lidar com a criptgaria de mensagens
        EncriptaDecripta criptogafiaBib = new EncriptaDecripta();

        //Instancia do relogio digital
        RelogioDigital relogioDigital = new RelogioDigital(false, 100L);

        //Aqui será instanciado a classe de controle ControleMulticast
        this.controleMulticast = new ControleMulticast(this);


        geraID();
        verificaMaster();
        imprimeGrid();

        //iniciando Threads

        //iniciamdo thread do relógio digital
        (new Timer()).schedule(relogioDigital, relogioDigital.getTaxaAtualizacao(), relogioDigital.getTaxaAtualizacao());

        //Iniciando listener multisoket
        this.controleMulticast.run();
    }

    public void masterAtivo() {
        Timer timer = new Timer();
        MasterAtivo masterAtivo = new MasterAtivo(id, this);
        timer.schedule(masterAtivo, 1000, 1000);
    }

    private void verificaMaster() {
        Timer timer = new Timer();
        verificaMaster = new VerificaMaster(this, id);
        timer.schedule(verificaMaster, 5000, 5000);
    }

    private void imprimeGrid() {
        Grid grid = new Grid(this);
        Timer gridTimer = new Timer();
        gridTimer.schedule(grid, 1000, 5000);
    }

    private void geraID() throws SocketException {
        UUID uuid = UUID.randomUUID();
        DatagramSocket unisocket = new DatagramSocket();
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
