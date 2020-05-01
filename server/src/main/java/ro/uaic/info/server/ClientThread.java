package ro.uaic.info.server;

import ro.uaic.info.net.Connection;
import ro.uaic.info.net.state.ClientState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private Connection connection;
    private final int threadID;
    private final boolean debuggingEnabled = true;

    public ClientThread(Socket socket, int threadID){
        this.socket = socket;
        this.connection = new Connection.ConnectionFactory()
                .withSocket(this.socket)
                .withDebugMode(this.debuggingEnabled)
                .build();
        this.threadID = threadID;
    }

    public void run(){
        while(true){
            String message = this.connection.readMessage();
            ClientState clientState = this.decodeState(message);

            System.out.println(clientState);

            if(clientState.equals(ClientState.CLIENT_EXIT) || !this.connection.isConnected() || clientState.equals(ClientState.UNKNOWN))
                break;

            this.connection.writeMessage("SER_ACK");
        }

        this.connection.disconnect();
    }

    public ClientState decodeState(String message){
        switch (message){
            case "REG_UPD": return ClientState.STATUS_UPDATE;
            case "CRT_LBY": return ClientState.CREATE_LOBBY;
            case "CLI_STP": return ClientState.CLIENT_EXIT;
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
