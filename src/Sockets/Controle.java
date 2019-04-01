package Sockets;

import java.io.IOException;
import java.net.*;
import java.security.PrivateKey;
import java.util.Enumeration;
import java.util.Timer;
import java.util.UUID;

public class Controle extends Thread{
    String id;
    PrivateKey privateKey;
    MulticastSocket multisocket;
    DatagramSocket unisocket;
    InetAddress group;
    int port = 6789;
    Grid grid;
    String multicastIP = "228.5.6.7";
    Multicast multicast;
    Tempo tempo;
    VerificaMaster verificaMaster;
    Processo processo;
    MasterAtivo masterAtivo;

    Controle() throws IOException {
        criaTempo();
        geraID();
        estabeleceMulticast();
        verificaMaster();
        criaProcesso();
        imprimeGrid();
    }

    public void masterAtivo() {
        Timer timer = new Timer();
        masterAtivo = new MasterAtivo(id);
        timer.schedule(masterAtivo, 1000, 1000);
    }

    private void criaTempo() {
        Timer timer = new Timer();
        tempo = new Tempo();
        timer.schedule(tempo, 1000, 1000);
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
    private void imprimeGrid(){
        grid = new Grid(this);
        Timer gridTimer = new Timer();
        gridTimer.schedule(grid, 1000, 5000);
    }

    private void geraID() throws SocketException {
        UUID uuid = UUID.randomUUID();
        unisocket = new DatagramSocket();
        id = uuid.toString().substring(0,4).concat(":".concat(Integer.toString(unisocket.getLocalPort())));
        System.out.println(id);
    }

    @Override
    public void run() {
        String msg = "";

        while (!msg.equals("exit")){
            msg = multicast.getMensagem();
            if (msg.equals("ola")){
                masterAtivo();
            }
        }
    }
}
