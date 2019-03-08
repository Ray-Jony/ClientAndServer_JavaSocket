package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author :
 * @version :
 */
public class MessageToAllThread extends Thread {
    String message;
    ServerThread serverThread;

    public MessageToAllThread(ServerThread serverThread, String message) {
        this.message = message;
        this.serverThread = serverThread;
        System.out.println("Server.MessageToAllThread - get: " + message);
        start();
    }

    @Override
    public void run() {
        //简洁的标志有助于提升传输速度
        message = "2" + serverThread.getUser() + "@" + message.substring(2);
        try {
            for (Socket s :
                    serverThread.getServer().getClients().values()) {
//                System.out.println("Server.MessageToAllThread - Clients list" + s);
                new PrintWriter(s.getOutputStream(),true).println(message);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
