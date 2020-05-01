package ro.uaic.info.server;

import ro.uaic.info.net.Connection;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.resource.Lobby;
import ro.uaic.info.resource.MatchmakingResources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;

public class ClientThread extends Thread {
    private Socket socket;
    private Connection connection;
    private final int threadID;
    private final boolean debuggingEnabled = true;
    private String username;

    private MatchmakingResources matchmakingResources;

    public ClientThread(Socket socket, int threadID, MatchmakingResources matchmakingResources){
        this.socket = socket;
        this.connection = new Connection.ConnectionFactory()
                .withSocket(this.socket)
                .withDebugMode(this.debuggingEnabled)
                .build();
        this.connection.initializeBuffers();
        this.threadID = threadID;
        this.matchmakingResources = matchmakingResources;
    }

    public void treatState(ClientState state){
        if(state.equals(ClientState.CLIENT_START)){
            this.connection.writeMessage("GET_USR");
            this.username = this.connection.readMessage();
            System.out.println("Received Username ... " + this.username);
            this.connection.writeMessage("SER_ACK");
            return;
        }

        if(state.equals(ClientState.CREATE_LOBBY)){
            this.matchmakingResources
                    .addLobby(
                            new Lobby(this.username)
                    );

            System.out.println("New Lobby List : " + this.matchmakingResources.getLobbyList());
            this.connection.writeMessage("SER_ACK");
            return;
        }

        if(state.equals(ClientState.STATUS_UPDATE)){
            this.connection.writeMessage("SND_MAT_LST");
            this.connection.writeMessage(this.matchmakingResources.getLobbyList().toString());
        }
    }

    public void run(){
        while(true){
            String message = this.connection.readMessage();
            if(message == null) {
                System.out.println("Received nothing. Client DC");
                break;
            }

            System.out.println("Received Message ... " + message);

            ClientState clientState = this.decodeState(message);

            if(!clientState.equals(ClientState.STATUS_UPDATE))
                System.out.println(clientState);

            if(clientState.equals(ClientState.CLIENT_EXIT)){
                System.out.println("Safe exit from client");
                break;
            }
            if(!this.connection.isConnected()){
                System.out.println("Unsafe exit from client");
                break;
            }
            if(clientState.equals(ClientState.UNKNOWN)) {
                System.out.println("Unknown client state");
                break;
            }

            this.treatState(clientState);
        }

        //this.connection.disconnect();
    }

    public ClientState decodeState(String message){
        switch (message){
            case "REG_UPD": return ClientState.STATUS_UPDATE;
            case "CRT_LBY": return ClientState.CREATE_LOBBY;
            case "CLI_STP": return ClientState.CLIENT_EXIT;
            case "CLI_STA": return ClientState.CLIENT_START;
            default :       return ClientState.UNKNOWN;
        }
    }

    public void printMessage(String message){
        System.out.println("[ THREAD ID " + this.threadID + " ] Message : " + message);
    }

    public void printError(String message){
        System.err.println("[ THREAD ID " + this.threadID + " ] Error : " + message);
    }
}
