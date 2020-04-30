package ro.uaic.info.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {

    public static final String  LOCALHOST       = "127.0.0.1";
    public static final int     DEFAULT_PORT    = 3100;
    public static final String  DEFAULT_ADDRESS = LOCALHOST;

    private             String  serverAddress;
    private             int     port;
    private             Socket  socket          = null;
    private             boolean isConnected     = false;

    private             boolean debugActive;

    public static class ConnectionFactory{

        private         String  serverAddress   = Connection.DEFAULT_ADDRESS;
        private         int     port            = Connection.DEFAULT_PORT;
        private         boolean activeDebug     = false;

        public ConnectionFactory withIPV4Address(String address){
            this.serverAddress = address;
            return this;
        }

        public ConnectionFactory withPort(int port){
            this.port = port;
            return this;
        }

        public ConnectionFactory withDebugMode(boolean toggle){
            this.activeDebug = toggle;
            return this;
        }

        public Connection build(){
            Connection connection       = new Connection();

            connection.serverAddress    = this.serverAddress;
            connection.port             = this.port;
            connection.debugActive      = this.activeDebug;

            return connection;
        }
    }

    private Connection(){

    }

    public void connect(){
        try {
            this.socket = new Socket(this.serverAddress, this.port);
            this.isConnected = true;
        }
        catch (IOException exception){
            System.err.println("Connection refused " + exception);
        }
    }

    public void disconnect(){
        if(this.socket == null)
            return;

        try{
            this.socket.close();
            this.isConnected = false;
        }
        catch (IOException exception){
            System.err.println("Connection close failure " + exception);
        }
    }

    public Socket getSocket() {
        if(!this.isConnected){
            System.err.println("Connection inactive");
            return null;
        }

        return this.socket;
    }

    public String readMessage(){
        if(!this.isConnected){
            System.err.println("Connection inactive. Have you called .connect()?");
            return null;
        }

        try{
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            this.socket.getInputStream()
                    )
            );

            if(this.debugActive)
                System.out.println(" ... Waiting for message ... ");

            return input.readLine();
        }
        catch(UnknownHostException exception){
            System.err.println("No server listening " + exception);
            return null;
        }
        catch (IOException exception){
            System.err.println("Socket read failure " + exception);
            return null;
        }
    }

    public void writeMessage(String message){
        if(!this.isConnected){
            System.err.println("Connection inactive. Have you called .connect()?");
            return;
        }

        try{
            PrintWriter output = new PrintWriter(
                    this.socket.getOutputStream(), true
            );

            if(this.debugActive)
                System.out.println(" ... Writing message ... ");

            output.println(message);
        }
        catch (IOException exception){
            System.err.println("Socket write failure" + exception);
        }
    }
}
