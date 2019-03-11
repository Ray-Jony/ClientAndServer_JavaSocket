package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageToSingleThread extends Thread {

    ServerThread serverThread;
    String message;
    String targetUser;


    public MessageToSingleThread(ServerThread serverThread, String message) {
        this.serverThread = serverThread;
        this.message = message;
        this.targetUser = message.substring(1, message.indexOf("@"));
        System.out.println("私聊线程已启动，待转发消息给[" + targetUser + "]：" + message.substring(message.indexOf("@")));
        start();
    }

    @Override
    public void run() {

        //TODO 检查user是否在线（是否存在于HashTable clients中）
        Socket userSocket = serverThread.getServer().getClients().get(targetUser);
        message = message.substring(message.indexOf("@") + 1);
        try {
            message = "3" + serverThread.getUser() + "@" + message;
            new PrintWriter(userSocket.getOutputStream(), true).println(message);
            System.out.println("私聊消息已发送");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
