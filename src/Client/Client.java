package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Client {

    private int flag;//if flag == 0 perform sendToUser else sendToGroup;
    private String user;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<String> onlineUserList;
    private boolean login;
    private boolean serverActive;
    private ClientUI clientUI;
    private ClientCheckServerActive sync;
    private ClientLoginThread clientLoginThread;
    private String reloginInfo;
    private ClientMessageOutThread clientMessageOutThread;
    private Hashtable<Integer, List<String>> JoinedGroups;

    public Client(String host, int port) throws IOException {
        this.onlineUserList = new ArrayList<>();
        this.login = false;
        this.clientUI = new ClientUI();
        this.socket = new Socket(host, port);
        this.serverActive = true;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.JoinedGroups = new Hashtable<>();
    }


    public void start() {
        sync = new ClientCheckServerActive(this);
        new ClientMessageInThread(this);
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
        clientMessageOutThread = new ClientMessageOutThread(this);


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

    public void register() {
        clientUI.getRegisterButton().addActionListener(e -> {

        });
    }

    public Hashtable<Integer, List<String>> getJoinedGroups() {
        return JoinedGroups;
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

    public void sendGroupCreationRequest(List<String> targetUser){//3GroupName@Apple@Microsoft@
        StringBuilder groupInfo = new StringBuilder();
        for (String u :
                targetUser) {
            groupInfo.append(u);
            groupInfo.append("@");
        }
        out.println("3" + groupInfo);
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
        return serverActive;
    }

    public void setServerActive(boolean serverActive) {
        this.serverActive = serverActive;
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

    public ClientCheckServerActive getSync() {
        return sync;
    }

    public ClientMessageOutThread getClientMessageOutThread() {
        return clientMessageOutThread;
    }

    public static void main(String[] args) throws IOException {
        Client localhost = new Client("localhost", 8000);
        localhost.start();
    }
}
