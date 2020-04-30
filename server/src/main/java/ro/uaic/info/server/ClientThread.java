package ro.uaic.info.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket = null;
    private final int threadID;

    public ClientThread(Socket socket, int threadID){
        this.socket = socket;
        this.threadID = threadID;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            this.printMessage("Waiting for message ... ");

            String request = in.readLine();

            this.printMessage("Got message ... ");

            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            String response = "Hello " + request + "!";

            this.printMessage("Sending message");

            out.println(response);
            out.flush();

            this.printMessage("Message Sent!");
        }
        catch (IOException exception){
            this.printError("Socket TCP communication error " + exception);
        }
        finally {
            try{
                this.socket.close();
            }
            catch (IOException exception){
                this.printError("Socket closure error " + exception);
            }
        }
    }

    public void printMessage(String message){
        System.out.println("[ THREAD ID " + this.threadID + " ] Message : " + message);
    }

    public void printError(String message){
        System.err.println("[ THREAD ID " + this.threadID + " ] Error : " + message);
    }
}
