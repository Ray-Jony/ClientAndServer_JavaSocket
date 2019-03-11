package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientInThread extends Thread {
    private Client client;

    public ClientInThread(Client client) {
        this.client = client;
        System.out.println("Client接受线程已启动");
        start();
    }

    @Override
    public void run() {
        //思考：每个socket都有一个独特的outputStream 所以需要新的判断，来分配readline的内容, 避免调用outputStream
        try {

            String message;
            message = client.getIn().readLine();

            while (message != null && client.isServerActive()) {
                System.out.println("Client收到来自Server的消息" + message);
                char Indicator = message.charAt(0);
                if (message.length() == 1) {
                    if (Indicator == '0') {
                        client.getClientLoginThread().setPermission("0");
                    } else if (Indicator == '1') {
                        client.getClientLoginThread().setPermission("1");
                    } else if (Indicator == '9') {
                        System.out.println("消息类型为：检查客户端状态");
                        client.sendHearBeats();
                        System.out.println("客户端心跳包已发送");
                    } else if (Indicator == '8') {
                        System.out.println("消息类型为：服务器心跳包");
                        client.sync.setTmpActive(true);
                        System.out.println("服务器连接正常");
                    }
                } else {
                    String sender = message.substring(1, message.indexOf("@"));
                    String realMessage = message.substring(message.indexOf("@") + 1);
                    if (Indicator == '2') {
                        System.out.println("消息类型为：接收到的群发消息");
                        System.out.println("[ALL] " + sender + ": " + realMessage);
                    } else if (Indicator == '3') {
                        System.out.println("消息类型为：接收到的私聊消息");
                        System.out.println("[Private] " + sender + ": " + realMessage);
                    } else if (Indicator == '4') {
                        //TODO 完成群组聊天功能
                        System.out.println("Function haven't finish");
                    } else if (Indicator == '8') {
                        System.out.println("消息类型为：接收到的用户列表");
                        message = message.substring(1);
                        List<String> onlineUserList = new ArrayList<>();
                        while (message.contains("@")) {
                            onlineUserList.add(message.substring(0, message.indexOf("@")));
                            message = message.substring(message.indexOf("@") + 1);
                        }
                        client.setOnlineUserList(onlineUserList);
                        System.out.println("已更新用户列表为：" + client.getOnlineUserList());
                        client.setServerActive(true);
                    } else {
                        System.out.println("***接收到无法识别的消息***");
                    }

                }
                message = client.getIn().readLine();
            }

        } catch (IOException e) {
            client.setServerActive(false);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
