package ro.uaic.info.app;

import ro.uaic.info.net.Connection;
import ro.uaic.info.window.MainWindow;

public class ClientApp {
    public static void initApp(){
        MainWindow mainWindow = new MainWindow();
        mainWindow.run();
    }

    public static void main(String[] args) {
        ClientApp.initApp();
    }
}


        /*
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

         */