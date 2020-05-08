package ro.uaic.info.net.handler;

import com.mifmif.common.regex.Main;
import ro.uaic.info.game.Player;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.net.state.ServerState;
import ro.uaic.info.window.MainWindow;

import java.util.LinkedList;
import java.util.Queue;

public class EventHandler extends Thread {
    public static final int DEFAULT_TICK_RATE = 16;

    private  Queue<String> messageQueue = new LinkedList<>();
    private int tickRate = DEFAULT_TICK_RATE;

    private int sleepTimer;

    private MainWindow parent;

    private boolean enableStop = false;

    private boolean threadLock = false;

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

                while(this.threadLock);

                if (this.messageQueue.peek().equals("CLI_STP"))
                    this.enableStop = true;

                pingNano = System.nanoTime();
                this.parent.getConnection().writeMessage(messageQueue.remove());

                if(this.enableStop)
                    break;

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
            } catch (InterruptedException ignored) {

            }

        }

    }

    public synchronized void treatServerResponse(ServerState state){
        System.out.println("Received state : " + state);

        //this.threadLock = true;

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
            return;
        }

        if(state.equals(ServerState.GAME_START_SUCCESS_WHITE)){
            this.parent.gameStartCallback("WHITE");
            return;
        }

        if(state.equals(ServerState.GAME_START_SUCCESS_BLACK)){
            this.parent.gameStartCallback("BLACK");
            return;
        }

        if(state.equals(ServerState.BLACK_PLAYER_TURN)){
            this.parent.getGamePanel().getGameBoardPanel().setTurn(
                    this.parent.getGamePanel().getGameBoardPanel().getTokenColour().equals("BLACK")
            );
            return;
        }
        if(state.equals(ServerState.WHITE_PLAYER_TURN)){
            this.parent.getGamePanel().getGameBoardPanel().setTurn(
                    this.parent.getGamePanel().getGameBoardPanel().getTokenColour().equals("WHITE")
            );
            return;
        }
        if(state.equals(ServerState.SENDING_BOARD)){
            this.parent.getGamePanel().getGameBoardPanel().getJSONEncodedMatrix(this.parent.getConnection().readMessage());
        }

        this.threadLock = false;
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
            case REQUEST_BOARD: this.addToMessageQueue("REQ_BRD");   break;
            case PUT_PIECE:     this.addToMessageQueue("PUT_PIE");   break;
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
            case "GAM_STA_SUC_WHI" : return ServerState.GAME_START_SUCCESS_WHITE;
            case "GAM_STA_SUC_BLA" : return ServerState.GAME_START_SUCCESS_BLACK;
            case "WHITE_TURN"  : return ServerState.WHITE_PLAYER_TURN;
            case "BLACK_TURN"  : return ServerState.BLACK_PLAYER_TURN;
            case "GET_PIE"     : return ServerState.WAITING_FOR_PIECE_LOCATION;
            case "SND_MAP"     : return ServerState.SENDING_BOARD;
            default: return ServerState.UNKNOWN;
        }
    }

    public void addToMessageQueue(String message){
        messageQueue.add(message);
    }
}
