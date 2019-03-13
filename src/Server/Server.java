package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.List;

public class Server {

    int port;
    public ServerSocket ss;
    public Hashtable<String, Socket> clients = new Hashtable<>();
    public Hashtable<Integer, List<String>> groups = new Hashtable<>(); // note that the first String of the groups store the group name;
    public int groupId;
//   final int TIMOUT = 3600; Set a timer to close the server when there is no connection

    public Server(int port) throws IOException {
        this.port = port;
        ss = new ServerSocket(port);
        System.out.println("服务器已搭建，端口号：" + port);
    }

    public void start() throws IOException {
        System.out.println("服务器已启动，端口号：" + port);
        getConnections();
    }

    public void getConnections() throws IOException {
        while (true) {
            Socket socket = ss.accept();
            new ServerThread(this, socket);
        }
    }

    public Hashtable<String, Socket> getClients() {
        return clients;
    }

    public Hashtable<Integer, List<String>> getGroups() {
        return groups;
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8000);
        server.start();
    }
}
