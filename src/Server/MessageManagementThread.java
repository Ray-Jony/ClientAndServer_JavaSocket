package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author :
 * @version :
 */
public class MessageManagementThread extends Thread {
    ServerThread serverThread;

    public MessageManagementThread(ServerThread serverThread) {

        this.serverThread = serverThread;
        System.out.println( serverThread.getUser() + "'s MessageManagementThread have been created%n");
        //TODO the start method should be called
        start();
    }


    @Override
    public void run() {
        try {
            String message = serverThread.getIn().readLine();
            System.out.println(message);
//            out.println("send over Server.MessageManagementThread" + message);
//            new PrintWriter(serverThread.getSocket().getOutputStream(), true).println("send over Server.MessageManagementThread" + message);
            while (message != null) {
                if (message.charAt(0) == '0') {
                    new MessageToAllThread(serverThread, message);
                } else if (message.charAt(0) == '1') {
                    new MessageToSingleThread(serverThread, message);
                } else if (message.charAt(0) == '3') {
                    System.out.println(message);
                    //TODO 完成群组聊天功能
                    System.out.println("Function haven't finish");
                }else if (message.charAt(0) == '9'){
                    serverThread.setActive(true);
                    System.out.println("From MMT: Set Active True");
                }
                else {
                    System.out.println("***Unidentified Message***");
                }
                message = serverThread.getIn().readLine();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
