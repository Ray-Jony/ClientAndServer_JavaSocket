package Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientMessageOutThread extends Thread {
    private Client client;
    private Scanner scanner;

    public ClientMessageOutThread(Client client) {
        this.client = client;
        System.out.println("[" + client.getUser() + "]的输出线程已启动");
        this.scanner = new Scanner(System.in);
        start();
    }

    @Override
    public void run() {
        while (client.isServerActive()) {
            System.out.println("请选择你想要发送什么类型的信息：");
            System.out.printf("1.发给所有人 \n2.发给特定用户\n3.发给群聊\n4.新建群聊\n5.查看当前在线用户");
            String in = scanner.nextLine();
            if (in.equals("1")) {
                System.out.println("键入你想发送给所有人的信息:");
                String message = scanner.nextLine();
                if (message != null)
                    client.sendToAll(message);
                else System.out.println("消息不能为空！");
            } else if (in.equals("2")) {
                System.out.println("你想要发给谁？");
                System.out.println(client.getOnlineUserList());
                String targetUser = scanner.nextLine();
                if (true) {//TODO 添加判断 targetUser是否在列表中。
                    System.out.println("键入你想发给[" + targetUser + "]的信息:");
                    String message = scanner.nextLine();
                    if (message != null)
                        client.sendToSingle(targetUser, message);
                    else System.out.println("消息不能为空！");
                }
            } else if (in.equals("3")) {
                System.out.println("已加入的群聊：");
                if (client.getJoinedGroups() == null) {
                    System.out.println("你目前没有加入任何群聊");
                    continue;
                }
                System.out.println(client.getJoinedGroups());
                System.out.println("你想给哪个群聊发送信息？");
                String targetGroup = scanner.nextLine();
                System.out.println("键入你想发送给[" + targetGroup + "]的信息：");
                String message = scanner.nextLine();
                client.sendToGroup(Integer.parseInt(targetGroup), message);
            } else if (in.equals("4")) {
                System.out.println("你想邀请谁加入群聊？第一位输入群聊名称，键入#以结束");
                System.out.println(client.getOnlineUserList());
                List<String> targetUser = new ArrayList<>();
                while (true) {
                    String tmp = scanner.nextLine();
                    if (tmp.equals("#"))
                        break;
                    targetUser.add(tmp);
                }
                client.sendGroupCreationRequest(targetUser);

            } else if (in.equals("5")) {
                System.out.println(client.getOnlineUserList());
            } else {
                System.out.println("键入的信息未知");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("ClintOutThread已停止工作");

    }


    public Scanner getScanner() {
        return scanner;
    }
}
