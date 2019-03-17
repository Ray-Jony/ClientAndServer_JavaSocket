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
    private boolean clientActive;
    private boolean tmpActive;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.login = false;
        this.clientActive = false;
        start();
    }

    /**
     * 在服务器端为客户端开设专一线程，处理该客户端的消息。
     */
    @Override
    public void run() {
        try {
            System.out.println("与[" + socket.getInetAddress() + "]的连接已建立");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String message;
            String username;

            /*
              1.建立好连接后的第一件事就是验证身份
             */

            while (true) {
                message = in.readLine();
                if (message.charAt(0) == '8'){
                    System.out.println("收到消息：检查服务器状态");
                    out.println("8");
                    System.out.println("服务器心跳包已发送");
                    continue;
                }
                System.out.println("等待验证身份...");
                String[] loginInfo = message.split("@");
                username = loginInfo[0];

                login = new DBHelper().checkUser(loginInfo[0], loginInfo[1]);

                if (login) {
                    out.println("1");
                    System.out.println("身份验证成功，用户：" + username + "已登录");
                    break;
                } else
                    System.out.println("身份验证失败");
                out.println("0");
            }

            /*
              2.通过身份验证之后 来到这一步
             * */
            //绑定用户的用户名和通讯Socket， 储存在服务器
            server.clients.put(username, socket);
            server.clientsOut.put(username, out);
            System.out.println("用户：" + username + "已储存到服务列表");
            sendUserList();

            out.println(8);
            this.user = username;
            this.clientActive = true;


            /*
            3.判断消息类型
            * */
            new MessageManagementThread(this);//仅传递this就够了 设置in和out的getter

            //TODO 需要一个线程来同步服务器和客户端的用户列表。列表不能无限制刷新，应设置间隔时间


            //TODO 或者在这个线程里面来判断和刷新用户列表，并且检测连接情况，如果连接断开 则结束本线程


            //TODO 用户登出需要从列表删掉，结束线程

            tmpActive = isClientActive();
            while (tmpActive) {

                tmpActive =false;

                out.println("9");
                Thread.sleep(5 * 1000);
            }

            System.out.println("已失去与用户[" + user + "]的连接");
            server.getClients().remove(user);
            sendUserList();
            socket.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }

    public void sendUserList() {
        StringBuilder userList = new StringBuilder("8");
        for (String s : this.server.getClients().keySet()) {
            userList.append(s);
            userList.append("@");
        }
        System.out.println("获取用户列表： " + userList.toString());
        setClientActive(true);

        try {
            for (Socket s :
                    this.getServer().getClients().values()) {
                new PrintWriter(s.getOutputStream(), true).println(userList.toString() + "SendFromServer");
            }
            System.out.println("用户列表发送完毕");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToClient(String message){
        out.println(message);
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

    public boolean isClientActive() {
        return clientActive;
    }

    public void setClientActive(boolean clientActive) {
        this.clientActive = clientActive;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public boolean isLogin() {
        return login;
    }

    public void setTmpActive(boolean tmpActive) {
        this.tmpActive = tmpActive;
    }
}
