package Sockets;

import java.io.IOException;
import java.util.TimerTask;

public class TimeReminder extends TimerTask {

    Multicast multicast;

    TimeReminder(Multicast multicast){
        this.multicast = multicast;
    }

    @Override
    public void run() {
        System.out.println("Time's up!");
        multicast.setMessage("MASTER/" + multicast.self.getId());
        try {
            multicast.sendMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.cancel(); //Terminate the timer thread
        }
    }
}
