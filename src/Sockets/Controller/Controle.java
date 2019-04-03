package Sockets.Controller;

import Sockets.*;
import Sockets.Model.Processo;
import Sockets.View.Grid;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.sql.Time;
import java.util.Timer;
import java.util.UUID;

public class Controle extends Thread {
    public String id;
    public Multicast multicast;
    public VerificaMaster verificaMaster;
    PrivateKey privateKey;
    MulticastSocket multisocket;
    DatagramSocket unisocket;
    InetAddress group;
    int port = 6789;
    Grid grid;
    String multicastIP = "228.5.6.7";
    RelogioDigital relogioDigital;
    Processo processo;
    MasterAtivo masterAtivo;

    //Esta é o contrutor da principal classe do processo, nele será intanciada e ativada todas as threads de processo
    public Controle() throws IOException {

        //Instancia do relogio digital
        this.relogioDigital = new RelogioDigital(false, 100L);



        geraID();
        estabeleceMulticast();
        verificaMaster();
        criaProcesso();
        imprimeGrid();

        //iniciando Threads

        //iniciamdo thread do relógio digital
        (new Timer()).schedule(this.relogioDigital,this.relogioDigital.getTaxaAtualizacao(),this.relogioDigital.getTaxaAtualizacao());
    }

    public void masterAtivo() {
        Timer timer = new Timer();
        masterAtivo = new MasterAtivo(id);
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

    private void estabeleceMulticast() throws IOException {
        group = InetAddress.getByName(multicastIP);
        multisocket = new MulticastSocket(port);
        multisocket.joinGroup(group);
        multicast = new Multicast(port, group, multisocket, this);
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
        String msg = "";

        while (!msg.equals("exit")) {
            msg = multicast.getMensagem();
            if (msg.equals("ola")) {
                masterAtivo();
            }
        }
    }
}
