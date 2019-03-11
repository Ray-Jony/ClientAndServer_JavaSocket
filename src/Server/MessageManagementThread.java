package Server;

import java.io.IOException;


/**
 * @author :
 * @version :
 */
public class MessageManagementThread extends Thread {
    ServerThread serverThread;

    public MessageManagementThread(ServerThread serverThread) {

        this.serverThread = serverThread;
        System.out.println("[" + serverThread.getUser() + "]的接受线程已启动");
        //TODO the start method should be called
        start();
    }


    @Override
    public void run() {
        try {
            String message = serverThread.getIn().readLine();
//            out.println("send over Server.MessageManagementThread" + message);
//            new PrintWriter(serverThread.getSocket().getOutputStream(), true).println("send over Server.MessageManagementThread" + message);
            while (message != null) {
                System.out.println("Server收到来自" + serverThread.getUser() + "消息：" + message);
                char Indicator = message.charAt(0);
                if (Indicator == '0') {
                    System.out.println("消息类型为：群发");
                    new MessageToAllThread(serverThread, message);
                } else if (Indicator == '1') {
                    System.out.println("消息类型为：私聊");
                    new MessageToSingleThread(serverThread, message);
                } else if (Indicator == '3') {
                    System.out.println(message);
                    //TODO 完成群组聊天功能
                    System.out.println("Function haven't finish");
                } else if (Indicator == '9') {
                    System.out.println("消息类型为：客户端心跳包");
                    serverThread.setTmpActive(true);
                    System.out.println("设置Client为Active");
                } else if (Indicator == '8') {
                    System.out.println("消息类型为：检查服务器状态");
                    serverThread.getOut().println("8");
                    System.out.println("服务器心跳包已发送");

                } else {
                    System.out.println("***Unidentified Message Received***");
                }
                message = serverThread.getIn().readLine();
            }

        } catch (IOException e) {
            serverThread.setClientActive(false);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
