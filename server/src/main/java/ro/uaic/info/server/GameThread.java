package ro.uaic.info.server;

import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.resource.GameResources;
import ro.uaic.info.resource.object.Board;

public class GameThread {
    private ClientThread currentThread;
    private ClientThread opponentThread;

    private Board board;

    private boolean gameActive = false;

    private boolean turnActive;
    private String colour;

    public boolean isTurnActive() {
        return turnActive;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
        this.turnActive = this.colour.equals("WHITE");
        this.board = this.colour.equals("WHITE") ?
                new Board(GameResources.DEFAULT_BOARD_WIDTH, GameResources.DEFAULT_BOARD_HEIGHT) :
                null;
    }

    public void turnValueChanged(boolean value){
        System.out.println("...turns changed...");
    }

    public void setTurnActive(boolean turnActive) {
        this.turnValueChanged(turnActive);
        this.turnActive = turnActive;
    }

    public GameThread(ClientThread playerThread){
        this.currentThread = playerThread;
        this.opponentThread = this.currentThread.getOpponentClientThread();
        this.gameActive = true;
    }

    private Board getBoard(){
        switch(this.colour) {
            case "WHITE":   return this.board;
            case "BLACK":   return this.opponentThread.getGameThread().board;
            default:        return null;
        }
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void debugMessage(ClientState clientState){
        if(clientState.equals(ClientState.CLIENT_EXIT)){
            System.out.println("Safe exit from client");
            this.gameActive = false;
            return;
        }
        if(!this.currentThread.getConnection().isConnected()){
            System.out.println("Unsafe exit from client");
            this.gameActive = false;
            return;
        }
        if(clientState.equals(ClientState.UNKNOWN)) {
            System.out.println("Unknown client state");
            this.gameActive = false;
        }
    }

    public void sendTurnStatus(){
        this.currentThread.getConnection().writeMessage(
                this.turnActive ?
                        (this.colour.toUpperCase() + "_TURN") :
                        (this.opponentThread.getGameThread().colour.toUpperCase() + "_TURN"));
    }

    public void treatState(ClientState state){
        switch (state){
            case STATUS_UPDATE:     this.sendTurnStatus();          return;
            case CLIENT_EXIT:       this.gameActive = false;
        }
    }

    public void treatMessage(String message){
        ClientState state = this.decodeState(message);

        System.out.println(state);

        this.debugMessage(state);

        if(this.gameActive)
            this.treatState(state);
    }

    public ClientState decodeState(String message){
        switch (message){
            case "REG_UPD" : return ClientState.STATUS_UPDATE;
            case "CLI_STP" : return ClientState.CLIENT_EXIT;
            default        : return ClientState.UNKNOWN;
        }
    }
}