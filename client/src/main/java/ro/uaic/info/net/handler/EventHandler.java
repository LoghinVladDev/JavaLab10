package ro.uaic.info.net.handler;

import com.mifmif.common.regex.Main;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.net.state.ServerState;
import ro.uaic.info.window.MainWindow;

import java.util.LinkedList;
import java.util.Queue;

public class EventHandler extends Thread {
    public static final int DEFAULT_TICK_RATE = 2;

    private  Queue<String> messageQueue = new LinkedList<>();
    private int tickRate = DEFAULT_TICK_RATE;

    private int sleepTimer;

    private MainWindow parent;

    public EventHandler(MainWindow parent){
        this.parent = parent;
        this.sleepTimer = 1000 / this.tickRate;
    }

    public synchronized void run(){
        while(true){
            try {
                //synchronized (MainWindow.getLock()) {
                    //MainWindow.getLock().wait(1000);
                    //System.out.println("tick");
                    Thread.sleep(this.tickRate);
                    //System.out.println(messageQueue);

                    if(messageQueue.isEmpty()){
                        this.addToMessageQueue(ClientState.STATUS_UPDATE);
                    }

                    this.parent.getConnection().writeMessage(messageQueue.remove());

                    String message = this.parent.getConnection().readMessage();

                    System.out.println(decodeServerState(message));

                    //MainWindow.getLock().notifyAll();
                    //}
            }
            catch (InterruptedException e){

            }

        }

    }

    public void addToMessageQueue(ClientState state){
        switch(state){
            case STATUS_UPDATE: addToMessageQueue("REG_UPD");   break;
            case CREATE_LOBBY:  addToMessageQueue("CRT_LBY");   break;
            case CLIENT_EXIT:   addToMessageQueue("CLI_STP");   break;
        }
    }

    public ServerState decodeServerState(String message){
        switch(message){
            case "SER_ACK" : return ServerState.DO_NOTHING;
            default: return ServerState.UNKNOWN;
        }
    }

    public  void addToMessageQueue(String message){
        messageQueue.add(message);
    }
}
