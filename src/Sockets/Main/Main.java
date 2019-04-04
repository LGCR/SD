package Sockets.Main;

import Sockets.Controller.Controle;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //ControleMulticast m = new ControleMulticast();
        //m.start();
        /*Timer timer = new Timer();
         */
        new Controle();
    }
}
