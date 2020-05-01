package ro.uaic.info.net.handler;

import com.mifmif.common.regex.Main;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.window.MainWindow;

import java.util.LinkedList;
import java.util.Queue;

public class EventHandler extends Thread {
    public static final int DEFAULT_TICK_RATE = 2;

    private static Queue<String> messageQueue = new LinkedList<>();
    private int tickRate = DEFAULT_TICK_RATE;

    private int sleepTimer;

    private MainWindow parent;

    public EventHandler(MainWindow parent){
        this.parent = parent;
        this.sleepTimer = 1000 / this.tickRate;
    }

    public synchronized void run(){
        try {
            Thread.sleep(5000);
        }
        catch (Exception e){

        }
/*
        while(true){
            try {
                synchronized (MainWindow.getLock()) {
                    MainWindow.getLock().wait(1000);
                    //System.out.println("tick");
                    System.out.println(messageQueue);

                    MainWindow.getLock().notifyAll();

                }
            }
            catch (InterruptedException e){

            }

        }*/

    }

    public static void addToMessageQueue(ClientState state){
        switch(state){
            case STATUS_UPDATE: addToMessageQueue("REG_UPD");
                break;
            case CREATE_LOBBY: addToMessageQueue("CRT_LBY");
        }
    }

    public static void addToMessageQueue(String message){
        messageQueue.add(message);
    }
}
