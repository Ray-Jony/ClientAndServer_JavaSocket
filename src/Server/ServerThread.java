package Server;

import Database.DBHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Server server;
    private Socket socket;
    private String user;
    private boolean login;
    private boolean active;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.login = false;
        this.active = false;
        start();
    }

    /**
     * 在服务器端为客户端开设专一线程，处理该客户端的消息。
     */
    @Override
    public void run() {
        try {
            System.out.println("connection with " + socket.getInetAddress() + " established");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            String username;

            /*
              1.建立好连接后的第一件事就是验证身份
             */

            while (true) {
                message = in.readLine();
                String[] loginInfo = message.split("@");
                username = loginInfo[0];

                login = new DBHelper().checkUser(loginInfo[0], loginInfo[1]);

                if (login) {
                    out.println("1");
                    break;
                } else
                    out.println("0");
            }

            /*
              2.通过身份验证之后 来到这一步
             * */
            System.out.println(username + " is online" + socket);
            //绑定用户的用户名和通讯Socket， 储存在服务器
            server.clients.put(username, socket);
            this.user = username;


            /*
            3.判断消息类型
            * */
            new MessageManagementThread(this,in,out);

            //TODO 需要一个线程来同步服务器和客户端的用户列表。列表不能无限制刷新，应设置间隔时间
            //TODO 或者在这个线程里面来判断和刷新用户列表，并且检测连接情况，如果连接断开 则结束本线程

            while (active){
                StringBuilder userList = new StringBuilder("9");
                for (String s : server.getClients().keySet()) {
                    userList.append(s);
                    userList.append("@");
                }
                out.println(userList);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void Login(BufferedReader in, PrintWriter out) {

    }


    public Server getServer() {
        return server;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUser() {
        return user;
    }
}
