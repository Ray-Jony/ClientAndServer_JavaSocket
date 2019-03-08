import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageToSingleThread extends Thread {

    ServerThread serverThread;
    String message;


    public MessageToSingleThread(ServerThread serverThread, String message) {
        this.serverThread = serverThread;
        this.message = message;

        start();
    }

    @Override
    public void run() {
        String user = message.substring(1, message.indexOf("@"));
        //TODO 检查user是否在线（是否存在于HashTable clients中）
        Socket userSocket = serverThread.getServer().getClients().get(user);
        message = message.substring(message.indexOf("@")+1);
        try {
            message = "3" + serverThread.getUser() + "@" + message;
            new PrintWriter(userSocket.getOutputStream(),true).println(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
