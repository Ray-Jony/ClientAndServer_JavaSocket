import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server {

    int port;
    public ServerSocket ss;
    public Hashtable<String,Socket> clients = new Hashtable<>();
//   final int TIMOUT = 3600; Set a timer to close the server when there is no connection

    public Server(int port) throws IOException {
        this.port = port;
        ss = new ServerSocket(port);
        System.out.println("Server established");
    }

    public void start() throws IOException{
        getConnections();
    }

    public void getConnections() throws IOException {
        while(true){
            Socket socket = ss.accept();
            new ServerThread(this,socket);
        }
    }

    public Hashtable<String, Socket> getClients() {
        return clients;
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(8000);
        server.start();
    }
}
