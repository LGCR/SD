package Multicast;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Multicast extends Thread {
    @Override
    public void run() {
        multicast();
    }

    public void multicast() {
        String msg = "";
        String multicastIP = "228.5.6.7";
        MulticastSocket s = null;
        try {
            InetAddress group = InetAddress.getByName(multicastIP);
            s = new MulticastSocket(6789);
            s.joinGroup(group);
            while (!msg.equals("exit")) {
                System.out.println("Digite Mensagem");
                Scanner cmd = new Scanner(System.in);
                msg = cmd.nextLine();
                byte[] m = msg.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
                s.send(messageOut);
                byte[] buffer = new byte[1000];
                //for (int i = 0; i < 3; i++) {        // get messages from others in group
                    DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
                    s.receive(messageIn);
                    System.out.println("Received:" + new String(messageIn.getData()).trim());
                //}
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
}
