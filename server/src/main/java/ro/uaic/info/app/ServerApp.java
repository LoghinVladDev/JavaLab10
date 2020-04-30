package ro.uaic.info.app;

import ro.uaic.info.server.GameServer;

public class ServerApp {
    public static void main(String[] args) {
        GameServer server = GameServer.getInstance();
        server.start();
    }
}
