package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageToGroupThread extends Thread {

    private ServerThread serverThread;
    String message;
    int targetGroup;

    public MessageToGroupThread(ServerThread serverThread, String message) {
        this.serverThread = serverThread;
        this.message = message;
        this.targetGroup = Integer.valueOf(message.substring(1, message.indexOf("@")));
        start();
        System.out.println("群聊线程已启动：发送给群组[" + targetGroup + "]" + message.substring(message.indexOf("@") + 1));
    }

    //sample message turned in:  "2" + group + "@" + message // 223@Hello all!
    @Override
    public void run() {
        message = message.substring(message.indexOf("@") + 1);
        List<String> groupUsers = serverThread.getServer().getGroups().get(targetGroup);
        //groupUsers.remove(0);//因为第一位储存的是群组聊天的名称 注意 ！！！！！remove操作会直接删掉哈希表中的值 所以不能用！
        System.out.println("groupUsers: " + groupUsers);
        String groupMessage = 4 + serverThread.getUser() + "@" + targetGroup + "@" + message;
        System.out.println("groupMessage: " + groupMessage);
        try {
            int count = 0;
            for (String user :
                    groupUsers) {
                count++;
                if (count == 1)
                    continue;
                new PrintWriter(serverThread.getServer().getClients().get(user).getOutputStream(), true).println(groupMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//这里可能会出现的问题是发送过去之后 用户检查自己的并没有在该组中，所以客户端不应该处理相关消息。