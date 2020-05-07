package ro.uaic.info.server;

import ro.uaic.info.resource.MatchmakingResources;
import ro.uaic.info.resource.ServerResources;

import java.io.IOException;
import java.net.ServerSocket;

public class GameServer {
    public static final int DEFAULT_PORT        = 3100;
    public static final int DEFAULT_MAX_CLIENTS = 100;

    private static GameServer instance;

    private int threadCounter           = 0;
    private int port                    = DEFAULT_PORT;
    private int maxClients              = DEFAULT_MAX_CLIENTS;
    private ServerSocket serverSocket   = null;
    private boolean serverActive        = true;

    private MatchmakingResources    matchmakingResources;
    private ServerResources         serverResources;

    public void start(){
        if(this.serverSocket == null){
            System.err.println("Cannot start if socket is not bound, exit function");
            return;
        }

        this.matchmakingResources   = new MatchmakingResources();
        this.serverResources        = new ServerResources();

        while(this.serverActive){
            System.out.println("Waiting for Client Connection...");

            try {
                ClientThread thread = new ClientThread(this.serverSocket.accept(), ++this.threadCounter, this.matchmakingResources, this.serverResources);
                this.serverResources.addToThreads(thread);
                thread.start();
            }
            catch (IOException exception){
                System.out.println("Accept failure " + exception);
            }
            catch (NullPointerException exception){
                System.out.println("Server shutdown socket null");
            }
        }
    }

    public void stopServer(){
        this.serverActive = false;

        try{
            this.serverSocket.close();
        }
        catch (IOException exception){
            System.err.println("Server socket close failure. " + exception);
        }
    }

    private GameServer() {
        try{
            this.serverSocket = new ServerSocket(this.port, this.maxClients);
        }
        catch(IOException exception){
            System.err.println("Server Socket Creation Error " + exception);
        }
    }

    public static GameServer getInstance() {
        if(GameServer.instance == null){
            GameServer.instance = new GameServer();
        }
        return GameServer.instance;
    }
}
