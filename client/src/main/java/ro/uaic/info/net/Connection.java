package ro.uaic.info.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * If used in server, use Connection c = new Connection.ConnectionFactory.withSocket(this.serverSocket.accept()).withDebugMode(true).build();
 *      and after this, no connect() call is necessary.
 *
 * If used in client, use Connection c = new Connection.ConnectionFactory().withIPV4Address(Connection.LOCALHOST).withPort(Connection.DEFAULT_PORT).withDebugMode(true).build();
 *      and after this, call connect() to use.
 */
public class Connection {

    public static final String  LOCALHOST       = "127.0.0.1";
    public static final int     DEFAULT_PORT    = 3100;
    public static final String  DEFAULT_ADDRESS = LOCALHOST;

    private             String  serverAddress;
    private             int     port;
    private             Socket  socket          = null;
    private             boolean isConnected     = false;

    private             boolean debugActive;

    private             PrintWriter output;
    private             BufferedReader input;

    public static class ConnectionFactory{

        private         String  serverAddress   = Connection.DEFAULT_ADDRESS;
        private         int     port            = Connection.DEFAULT_PORT;
        private         boolean activeDebug     = false;
        private         Socket  socket          = null;

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

        public ConnectionFactory withSocket(Socket socket){
            this.socket = socket;
            return this;
        }

        public Connection build(){
            Connection connection       = new Connection();

            connection.serverAddress    = this.serverAddress;
            connection.port             = this.port;
            connection.debugActive      = this.activeDebug;

            if(this.socket != null) {
                connection.socket       = this.socket;
                connection.isConnected  = true;
            }

            return connection;
        }
    }

    private Connection(){

    }

    public void connect(){
        if(this.isConnected){
            System.out.println("Close previous connection first! Call .disconnect()");
            return;
        }

        try {
            this.socket = new Socket(this.serverAddress, this.port);
            this.isConnected = true;

            this.output = new PrintWriter(
                    this.socket.getOutputStream(), true
            );

            this.input = new BufferedReader(
                    new InputStreamReader(
                            this.socket.getInputStream()
                    )
            );
        }
        catch (IOException exception){
            System.err.println("Connection refused " + exception);
        }
    }

    public void initializeBuffers(){
        try {
            this.output = new PrintWriter(
                    this.socket.getOutputStream(), true
            );

            this.input = new BufferedReader(
                    new InputStreamReader(
                            this.socket.getInputStream()
                    )
            );
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
            System.out.println("Disconnected!");
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

    public boolean isConnected(){
        return this.isConnected;
    }

    public String readMessage(){
        if(!this.isConnected){
            System.err.println("Connection inactive. Have you called .connect()?");
            return null;
        }

        try{
            /*String message;

            if(this.debugActive)
                System.out.println(" ... Waiting for message ... ");

            char[] lengthArray = new char[32];
            System.out.println("Read int :" +  input.read(lengthArray, 0, 32));
            int length = Integer.parseInt(String.valueOf(lengthArray), 2);
            char[] messageArray = new char[length];
            int readLength = input.read(messageArray, 0, length);
            message = String.valueOf(lengthArray);

            if(this.debugActive)
                System.out.println(" ... Got message ... " + message + ", expected length = " + ", actual length = " + readLength);

             */

            return this.input.readLine();
        }
        catch(UnknownHostException exception){
            System.err.println("No server listening " + exception);
            this.isConnected = false;
            return null;
        }
        catch (IOException exception){
            System.err.println("Socket read failure " + exception);
            this.isConnected = false;
            return null;
        }
    }

    public void writeMessage(String message){
        if(!this.isConnected){
            System.err.println("Connection inactive. Have you called .connect()?");
            return;
        }

        //try{
        //char[] messageChar = message.toCharArray();
        //int length = messageChar.length;

        if(this.debugActive)
            System.out.println(" ... Writing message ... " + message);

        //output.println(message);
        //output.flush();

        //output.write(Arrays.copyOf(Integer.toBinaryString(length).toCharArray(),32), 0, 32);
        //output.write(messageChar, 0, length);

        this.output.println(message);
        //}
        //catch (IOException exception){
        //    System.err.println("Socket write failure" + exception);
        //}
    }
}
