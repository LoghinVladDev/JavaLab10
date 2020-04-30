package ro.uaic.info.server;

import ro.uaic.info.net.Connection;

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
        if(this.debuggingEnabled)
            this.printMessage("Reading...");

        String receivedMessage = this.connection.readMessage();

        if(this.debuggingEnabled)
            this.printMessage("Read...");

        if(this.debuggingEnabled)
            this.printMessage("Writing...");

        this.connection.writeMessage("Hello " + receivedMessage + "!");

        if(this.debuggingEnabled)
            this.printMessage("Wrote...");

        this.connection.disconnect();
    }

    public void printMessage(String message){
        System.out.println("[ THREAD ID " + this.threadID + " ] Message : " + message);
    }

    public void printError(String message){
        System.err.println("[ THREAD ID " + this.threadID + " ] Error : " + message);
    }
}
