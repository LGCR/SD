package Sockets.View;

import Sockets.Controller.Controle;

import java.util.TimerTask;

public class Grid extends TimerTask {
    Controle controle;

    public Grid(Controle controle) {
        this.controle = controle;
        warn();
    }

    private void warn() {
        System.out.println("Grid will be printed in 3 seconds...");
    }

    @Override
    public void run() {
        System.out.println("====================================================");
        System.out.println("| MY PROCESS ID: " + controle.id + " -------------------------------|");
        System.out.println("| MASTER: " + controle.verificaMaster.master.getMaster() + " --------------------------------------|");
        System.out.print("| SLAVES: ");
        /*for(int i = 0; i < controleMulticast.self.getSlaveSize(); i++){
            System.out.print(controleMulticast.self.getSlave(i) + "-");
        }*/
        System.out.println("|");
        System.out.println("|--------------------------------------------------|");
        System.out.println("| PROCESS | CURRENT TIMESTAMP | ESTIMATED | ADJUST |");
        System.out.println("|--------------------------------------------------|");
        System.out.println("====================================================");
        warn();
        //this.cancel();
    }
}
