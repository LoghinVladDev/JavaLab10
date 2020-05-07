package ro.uaic.info.net.handler;

import com.mifmif.common.regex.Main;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.net.state.ServerState;
import ro.uaic.info.window.MainWindow;

import java.util.LinkedList;
import java.util.Queue;

public class EventHandler extends Thread {
    public static final int DEFAULT_TICK_RATE = 1;

    private  Queue<String> messageQueue = new LinkedList<>();
    private int tickRate = DEFAULT_TICK_RATE;

    private int sleepTimer;

    private MainWindow parent;

    public EventHandler(MainWindow parent){
        this.parent = parent;
        this.sleepTimer = 1000 / this.tickRate;
    }

    public synchronized void run() {
        long pingNano = 0;

        this.addToMessageQueue(ClientState.CLIENT_START);
        this.addToMessageQueue(this.parent.getUsername());

        while (true) {
            try {
                //synchronized (MainWindow.getLock()) {
                //MainWindow.getLock().wait(1000);
                //System.out.println("tick");
                //System.out.println(messageQueue);

                if (messageQueue.isEmpty()) {
                    this.addToMessageQueue(ClientState.STATUS_UPDATE);
                }

                System.out.println(messageQueue);

                if (this.messageQueue.peek().equals("CLI_STP"))
                    break;

                pingNano = System.nanoTime();
                this.parent.getConnection().writeMessage(messageQueue.remove());

                String message = this.parent.getConnection().readMessage();
                pingNano = System.nanoTime() - pingNano;

                this.parent.setPing(
                        (int) Math.ceil(pingNano / Math.pow(10, 6))
                );

                if (message == null) {
                    this.treatServerFail();
                    break;
                }

                ServerState state = decodeServerState(message);

                //System.out.println(decodeServerState(message));

                this.treatServerResponse(state);

                Thread.sleep(this.sleepTimer);

                //MainWindow.getLock().notifyAll();
                //}
            } catch (InterruptedException e) {

            }

        }

    }

    public synchronized void treatServerResponse(ServerState state){
        System.out.println("Received state : " + state);

        if(state.equals(ServerState.SENDING_MATCH_LIST)){
            String lobbyListString = this.parent.getConnection().readMessage();
            System.out.println(lobbyListString);
            this.parent.getMatchmakingPanel().getLobbiesPanel().updateLobbies(lobbyListString);
            return;
        }

        if(state.equals(ServerState.JOIN_LOBBY_SUCCESS)) {
            this.parent.getMatchmakingPanel().getLobbiesPanel().joinLobby(true);
            return;
        }

        if(state.equals(ServerState.JOIN_LOBBY_FAIL_NO_LOBBY) || state.equals(ServerState.JOIN_LOBBY_FAIL_LOBBY_FULL)){
            this.parent.getMatchmakingPanel().getLobbiesPanel().joinLobby(false);
            return;
        }

        if(state.equals(ServerState.LEAVE_LOBBY_SUCCESS)){
            this.parent.getMatchmakingPanel().getLobbyPanel().leaveLobby();
        }

        if(state.equals(ServerState.GAME_START_SUCCESS)){
            this.parent.getMatchmakingPanel().setEnabled(false);
            this.parent.getMatchmakingPanel().setVisible(false);
        }
    }

    public synchronized void treatServerFail(){
        this.parent.setConnectionStatus("Server down!");
        this.parent.setPing(0);
    }

    public void addToMessageQueue(ClientState state){
        switch(state){
            case STATUS_UPDATE: this.addToMessageQueue("REG_UPD");   break;
            case CREATE_LOBBY:  this.addToMessageQueue("CRT_LBY");   break;
            case CLIENT_EXIT:   this.addToMessageQueue("CLI_STP");   break;
            case CLIENT_START:  this.addToMessageQueue("CLI_STA");   break;
            case JOIN_LOBBY:    this.addToMessageQueue("CLI_JON");   break;
            case DELETE_LOBBY:  this.addToMessageQueue("CLI_DEL");   break;
            case START_GAME:    this.addToMessageQueue("STA_GAM");   break;
            case LEAVE_LOBBY :  this.addToMessageQueue("CLI_LEV");
        }
    }

    public ServerState decodeServerState(String message){
        switch(message){
            case "GET_LBY" : return ServerState.WAITING_FOR_LOBBY_NAME;
            case "SER_ACK" : return ServerState.DO_NOTHING;
            case "GET_USR" : return ServerState.WAITING_FOR_USERNAME;
            case "SND_MAT_LST" : return ServerState.SENDING_MATCH_LIST;
            case "JIN_LBY_SUC" : return ServerState.JOIN_LOBBY_SUCCESS;
            case "JIN_LBY_INV" : return ServerState.JOIN_LOBBY_FAIL_NO_LOBBY;
            case "JIN_LBY_FUL" : return ServerState.JOIN_LOBBY_FAIL_LOBBY_FULL;
            case "LEV_LBY_SUC" : return ServerState.LEAVE_LOBBY_SUCCESS;
            case "GAM_STA_FAI" : return ServerState.GAME_START_FAILED;
            case "GAM_STA_SUC" : return ServerState.GAME_START_SUCCESS;
            default: return ServerState.UNKNOWN;
        }
    }

    public void addToMessageQueue(String message){
        messageQueue.add(message);
    }
}
