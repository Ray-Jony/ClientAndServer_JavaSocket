import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private boolean ServerActive;


    private int flag;//if flag == 0 perform sendToUser else sendToGroup;
    private String user;
    private String message;
    private Scanner scanner;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<String> onlineUserList;
    private boolean login;

    private ClientUI clientUI;


    public Client(String host, int port) throws IOException {

        this.login = false;
        clientUI = new ClientUI();

        socket = new Socket(host, port);
        scanner = new Scanner(System.in);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }


    public void start() {

        login();

        while (true) {
            try {
                Thread.sleep(500);
                if (login)
                    break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        new ClientInThread(this);
        new ClientOutThread(this);


    }


    public void login() {

        clientUI.getLoginButton().addActionListener(e -> {


            String username = clientUI.getUserText().getText();
            String password = clientUI.getPasswordText().getText();
            String loginInfo = username + "@" + password;
//
//            out.println(loginInfo);
//
//            try {
//                String permission = in.readLine();
//                if (permission.equals("1")) {
//                    clientUI.successfulLogin();
//                    this.user = username;
//                    this.login = true;
//                    clientUI.getFrame().dispose();
//
//                } else {
//                    clientUI.loginFailed();
//                }
//            } catch (IOException e1) {
//                System.out.println(e1.getMessage());
//                e1.printStackTrace();
//            }
            new ClientLoginThread(this, loginInfo);

        });

    }


    public void sendToAll(String message) {
        out.println("0" + "@" + message);
    }

    public void sendToSingle(String user, String message) {
        out.println("1" + user + "@" + message);
    }

    public void sendToGroup(int group, String message) {//TODO define Group indicator
        out.println("2" + group + "@" + message);
    }


    public Scanner getScanner() {
        return scanner;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public List<String> getOnlineUserList() {
        return onlineUserList;
    }

    public boolean isServerActive() {
        return ServerActive;
    }

    public void setServerActive(boolean serverActive) {
        ServerActive = serverActive;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public ClientUI getClientUI() {
        return clientUI;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static void main(String[] args) throws IOException {
        Client localhost = new Client("localhost", 8000);
        localhost.start();
    }
}
