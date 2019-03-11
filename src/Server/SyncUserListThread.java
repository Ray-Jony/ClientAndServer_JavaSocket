package Server;

import java.io.PrintWriter;

/**
 * this Thread is to check if the user is still active
 * while to sync the online user list between the server and the client
 */
public class SyncUserListThread extends Thread{
    ServerThread serverThread;

    public SyncUserListThread(ServerThread serverThread) {
        this.serverThread =serverThread;
        start();
    }

    @Override
    public void run() {
//        while (serverThread.isClientActive()){
//            StringBuilder userList = new StringBuilder("9");
//            for (String s : serverThread.getServer().getClients().keySet()) {
//                userList.append(s);
//                userList.append("@");
//            }
//            serverThread.setClientActive(false);
//            serverThread.getOut().println(userList);
//            try {
//                Thread.sleep(3*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
