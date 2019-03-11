package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private int flag;//if flag == 0 perform sendToUser else sendToGroup;
    private String user;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<String> onlineUserList;
    private boolean login;
    private boolean ServerActive;
    private ClientUI clientUI;
    ClientCheckServerActive sync;
    ClientLoginThread clientLoginThread;
    private String reloginInfo;
    private ClientOutThread clientOutThread;

    public Client(String host, int port) throws IOException {
        this.onlineUserList = new ArrayList<>();
        this.login = false;
        this.clientUI = new ClientUI();
        this.socket = new Socket(host, port);
        this.ServerActive = true;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

    }


    public void start() {
        sync = new ClientCheckServerActive(this);
        new ClientInThread(this);
        login();
        while (true) {
            try {
                Thread.sleep(500);
                if (login) {
                    System.out.println("欢迎[" + this.user + "]，您已登入");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clientOutThread = new ClientOutThread(this);



    }


    public void login() {

        clientUI.getLoginButton().addActionListener(e -> {

            String username = clientUI.getUserText().getText();
            String password = clientUI.getPasswordText().getText();
            String loginInfo = username + "@" + password;

            clientLoginThread = new ClientLoginThread(this, loginInfo);

        });

    }
    //TODO 添加注册模块

    public void register(){
        clientUI.getRegisterButton().addActionListener(e-> {

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

    public void sendHearBeats() {
        out.println("9");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setReloginInfo(String reloginInfo) {
        this.reloginInfo = reloginInfo;
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

    public void setOnlineUserList(List<String> onlineUserList) {
        this.onlineUserList = onlineUserList;
    }

    public boolean isServerActive() {
        return ServerActive;
    }

    public void setServerActive(boolean serverActive) {
        ServerActive = serverActive;
    }

    public boolean isLogin() {
        return login;
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

    public ClientLoginThread getClientLoginThread() {
        return clientLoginThread;
    }


    public ClientOutThread getClientOutThread() {
        return clientOutThread;
    }

    public static void main(String[] args) throws IOException {
        Client localhost = new Client("localhost", 8000);
        localhost.start();
    }
}
