package Sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;

public class Multicast extends Thread{

    String mes;
    ArrayList<Peer> peers;
    Peer self;
    MulticastSocket s;
    InetAddress group;
    int greater = -1;

    Multicast(){
        setMessage("HELLO");
        this.peers = new ArrayList<>();
    }

    void setMessage(String msg){
        this.mes = msg;
    }

    String getMessage(){return this.mes;}

    @Override
    public void run() {
        multicast();
    }

    public void multicast() {
        String msg = "";
        String multicastIP = "228.5.6.7";
        s = null;
        try {
            group = InetAddress.getByName(multicastIP);
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            while (!msg.equals("exit")) {
                sendMessage();
                byte[] buffer = new byte[1000];
                //for (int i = 0; i < 3; i++) {        // get messages from others in group
                DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                msg = new String(messageIn.getData()).trim();
                if(msg.equals("HELLO")){
                    self = new Peer();
                    peers.add(self);
                    setMessage("GROUP SIZE/" + (peers.size() - 1));
                    sendMessage();
                }
                if (msg.startsWith("GROUP SIZE")){
                    String m[] = msg.split("/");
                    int size = Integer.parseInt(m[1]);
                    if(size > greater){
                        greater = size;
                    }
                    peers.get(peers.indexOf(self)).setId("P" + greater);
                    setMessage("WHO IS MASTER?");
                    sendMessage();
                }
                if (msg.equals("WHO IS MASTER?")){
                    if(self.getMaster() != null){
                        System.out.println(self.getId());
                        setMessage("MASTER/" + self.getMaster());
                        sendMessage();
                    }
                    else {
                        setMessage("IDLE");
                        sendMessage();
                        Timer timer = new Timer();
                        timer.schedule(new TimeReminder(this), 3000);
                    }
                }
                if(msg.startsWith("MASTER")){
                    String master[] = msg.split("/");
                    self.setMaster(master[1]);
                    for (int i = 0; i < peers.size(); i++) {
                        peers.get(i).setMaster(self.getMaster());
                    }
                    setMessage("IDLE");
                    sendMessage();
                }
                if(msg.startsWith("SLAVE")){
                    String master[] = msg.split("/");
                    self.setSlave(master[1]);
                    for(int i = 0; i < peers.size(); i++){
                        if (!peers.get(i).hasSlave(master[1])){
                            peers.get(i).setSlave(self.getSlave(i));
                        }
                    }
                    setMessage("IDLE");
                    sendMessage();
                }
                //printProcessGrid();
            }
            s.leaveGroup(group);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (s != null) s.close();
        }
    }

    private void printProcessGrid() {
        System.out.println("====================================================");
        System.out.println("| MY PROCESS ID: " + self.getId() + " -------------------------------|");
        System.out.println("| MASTER: " + self.getMaster() + " --------------------------------------|");
        System.out.print("| SLAVES: ");
        for(int i = 0; i < self.getSlaveSize(); i++){
            System.out.print(self.getSlave(i) + "-");
        }
        System.out.println("|");
        System.out.println("|--------------------------------------------------|");
        System.out.println("| PROCESS | CURRENT TIMESTAMP | ESTIMATED | ADJUST |");
        System.out.println("|--------------------------------------------------|");
        System.out.println("====================================================");
    }

    void sendMessage() throws IOException {
        byte [] m = getMessage().getBytes();
        DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
        s.send(messageOut);
    }
}
