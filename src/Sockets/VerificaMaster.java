package Sockets;

import Sockets.Controller.Controle;

import java.util.TimerTask;

public class VerificaMaster extends TimerTask {

    public Master master;
    Controle controle;
    String id;

    public VerificaMaster(Controle controle, String id) {
        this.controle = controle;
        this.id = id;
        master = new Master();
    }

    @Override
    public void run() {
        if (controle.multicast.getMensagem().startsWith("master ativo/")) {
            String[] master = controle.multicast.getMensagem().split("/");
            this.master.setMaster(master[1]);
        } else {
            this.master.setMaster(id);
            controle.masterAtivo();
            this.cancel();
        }
    }

    public class Master {
        String master = " ";

        public String getMaster() {
            return master;
        }

        private void setMaster(String master) {
            this.master = master;
        }
    }
}
