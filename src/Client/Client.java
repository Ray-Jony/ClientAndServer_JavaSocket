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
    //    private ClientUI clientUI;

    private String user;
    private String currentSelectedUser;
    private int currentSelectedGroup;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<String> onlineUserList;
    private boolean login;
    private boolean serverActive;

    private ClientCheckServerActive sync;
    private ClientLogin clientLogin;
    private String reLoginInfo;
    private ClientMessageOutThread clientMessageOutThread;
    private ClientMessageInThread clientMessageInThread;
    private Hashtable<Integer, List<String>> JoinedGroups;

    public ArrayList<UserListListener> userListUserListListeners = new ArrayList<>();
    public ArrayList<MessageListener> messageListeners = new ArrayList<>();

    public Hashtable<String, ArrayList<String>> singleConversations = new Hashtable<>();
    public Hashtable<Integer, ArrayList<String>> groupConversations = new Hashtable<>();
    public ArrayList<String> publicConversations = new ArrayList<>();


//    public static void main(String[] args) throws IOException {
//        Client localhost = new Client("localhost", 8000);
//        localhost.start();
//    }


    public Client(String host, int port) throws IOException {

//        this.clientUI = new ClientUI();
        this.onlineUserList = new ArrayList<>();
        this.login = false;
        this.socket = new Socket(host, port);
        this.serverActive = true;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.JoinedGroups = new Hashtable<>();
        this.clientLogin = new ClientLogin(this);
        this.sync = new ClientCheckServerActive(this);
        this.clientMessageInThread = new ClientMessageInThread(this);
        this.currentSelectedUser = null;
        this.currentSelectedGroup = -1;
    }


//    public void start() {
//        sync = new ClientCheckServerActive(this);
//        new ClientMessageInThread(this);
////        login();
//        while (true) {
//            try {
//                Thread.sleep(500);
//                if (login) {
//                    System.out.println("欢迎[" + this.user + "]，您已登入");
//                    break;
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        clientMessageOutThread = new ClientMessageOutThread(this);
//
//
//    }


//    public void login() {
//
//        clientUI.getLoginButton().addActionListener(e -> {
//
//            String username = clientUI.getUserText().getText();
//            String password = clientUI.getPasswordText().getText();
//            String loginInfo = username + "@" + password;
//
//            clientLogin = new ClientLogin(this, loginInfo);
//
//        });
//    }
    //TODO 添加注册模块


    public void close() {
        try {
            this.socket.close();
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.out.close();
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

    public void sendGroupCreationRequest(List<String> targetUser) {//3GroupName@Apple@Microsoft@
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

    public void addUserListListener(UserListListener userListListener) {
        userListUserListListeners.add(userListListener);
    }

    public void addMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    public void recordSingleMessageLog(String user, String message) {
        if (singleConversations.keySet().contains(user))
            singleConversations.get(user).add(message);
        else {
            singleConversations.put(user, new ArrayList<>());
            singleConversations.get(user).add(message);
        }
    }

    public ArrayList<String> getSingleMessageLog(String user) {
        if (singleConversations.keySet().contains(user))
            return singleConversations.get(user);
        else {
            singleConversations.put(user, new ArrayList<>());
            return singleConversations.get(user);
        }
    }

    public void recordGroupMessageLog(int groupID, String message) {
        if (groupConversations.keySet().contains(groupID))
            groupConversations.get(groupID).add(message);
        else {
            groupConversations.put(groupID, new ArrayList<>());
            groupConversations.get(groupID).add(message);
        }
    }

    public ArrayList<String> getGroupMessageLog(int groupID) {
        if (groupConversations.keySet().contains(groupID))
            return groupConversations.get(groupID);
        else {
            groupConversations.put(groupID, new ArrayList<>());
            return groupConversations.get(groupID);
        }
    }

    public void recordPublicMessageLog(String message) {
        publicConversations.add(message);
    }

    public ArrayList<String> getPublicMessageLog() {
        return publicConversations;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setReLoginInfo(String reLoginInfo) {
        this.reLoginInfo = reLoginInfo;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ClientLogin getClientLogin() {
        return clientLogin;
    }

    public ClientCheckServerActive getSync() {
        return sync;
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setSync(ClientCheckServerActive sync) {
        this.sync = sync;
    }

    public void setClientLogin(ClientLogin clientLogin) {
        this.clientLogin = clientLogin;
    }

    public String getReLoginInfo() {
        return reLoginInfo;
    }


    public void setJoinedGroups(Hashtable<Integer, List<String>> joinedGroups) {
        JoinedGroups = joinedGroups;
    }


    public ClientMessageInThread getClientMessageInThread() {
        return clientMessageInThread;
    }

    public void setClientMessageInThread(ClientMessageInThread clientMessageInThread) {
        this.clientMessageInThread = clientMessageInThread;
    }

    public String getCurrentSelectedUser() {
        return currentSelectedUser;
    }

    public void setCurrentSelectedUser(String currentSelectedUser) {
        this.currentSelectedUser = currentSelectedUser;
    }

    public int getCurrentSelectedGroup() {
        return currentSelectedGroup;
    }

    public void setCurrentSelectedGroup(int currentSelectedGroup) {
        this.currentSelectedGroup = currentSelectedGroup;
    }

}
