package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageCrateGroupThread extends Thread {
    private ServerThread serverThread;
    private String message;
    private String groupName;
    private List<String> groupInfo;
    private int groupID;

    public MessageCrateGroupThread(ServerThread serverThread, String message) {//3GroupName@Apple@Microsoft@
        this.serverThread = serverThread;
        this.message = message;
        this.groupInfo = new ArrayList<>();
        this.groupName = message.substring(1, message.indexOf("@"));
        message = message.substring(1);
        while (message.contains("@")) {
            this.groupInfo.add(message.substring(0, message.indexOf("@")));
            message = message.substring(message.indexOf("@") + 1);
        }
        groupInfo.add(this.serverThread.getUser());
        System.out.println("创建群聊线程已启动：" + groupInfo);
        start();
    }

    @Override
    public void run() {//public Hashtable<Integer, List<String>> groups;
        message = message.substring(message.indexOf("@") + 1);
        if (serverThread.getServer().getGroups().keySet().size() == 0) {
            this.groupID = 1;
        } else {
            List<Integer> groupsID = new ArrayList<>(serverThread.getServer().getGroups().keySet());
            Collections.sort(groupsID);
            Collections.reverse(groupsID);
            this.groupID = groupsID.get(0) + 1;
        }

        serverThread.getServer().getGroups().put(groupID, groupInfo);
        System.out.println("已更新群组列表：" + serverThread.getServer().getGroups());
        String createGroupMessage = 5 + serverThread.getUser() + "@" + groupID + "@" + groupName + "@" + message + serverThread.getUser() + "@";
        System.out.println("createGroupMessage: " + createGroupMessage);
        try {
            int count = 0;
            for (String user :
                    groupInfo) {
                count++;
                if (count == 1)
                    continue;
                new PrintWriter(serverThread.getServer().getClients().get(user).getOutputStream(), true).println(createGroupMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
