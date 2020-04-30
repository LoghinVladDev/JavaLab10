package ro.uaic.info.app;

import ro.uaic.info.net.Connection;

public class ClientApp {
    public static void main(String[] args) {
        Connection connection = new Connection.ConnectionFactory()
                .withIPV4Address(Connection.LOCALHOST)
                .withPort(Connection.DEFAULT_PORT)
                .withDebugMode(true)
                .build();

        connection.connect();

        String sentMessage = "mesaj";

        connection.writeMessage(sentMessage);

        String message = connection.readMessage();

        System.out.println(message);

        connection.disconnect();
    }
}
