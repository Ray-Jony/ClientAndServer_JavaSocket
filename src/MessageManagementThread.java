
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author :
 * @version :
 */
public class MessageManagementThread extends Thread {
    ServerThread serverThread;
    BufferedReader in;
    PrintWriter out;


    public MessageManagementThread(ServerThread serverThread, BufferedReader in, PrintWriter out) {
        this.serverThread = serverThread;
        this.in = in;
        this.out = out;
        //TODO the start method should be called
        start();
    }


    @Override
    public void run() {
        try {
            String message = in.readLine();
            System.out.println(message);
//            out.println("send over MessageManagementThread" + message);
//            new PrintWriter(serverThread.getSocket().getOutputStream(), true).println("send over MessageManagementThread" + message);
            while (message != null) {
                if (message.charAt(0) == '0') {
                    new MessageToAllThread(serverThread, message);
                } else if (message.charAt(0) == '1') {
                    new MessageToSingleThread(serverThread, message);
                } else if (message.charAt(0) == '3') {
                    System.out.println(message);
                    //TODO 完成群组聊天功能
                    System.out.println("Function haven't finish");
                } else {
                    System.out.println("***Unidentified Message***");
                }
                message = in.readLine();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
