package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientInThread extends Thread {
    private Client client;

    public ClientInThread(Client client) {
        this.client = client;
        System.out.println("Client.ClientInThread Created");
        start();
    }

    @Override
    public void run() {
        //思考：每个socket都有一个独特的outputStream 所以需要新的判断，来分配readline的内容, 避免调用outputStream
        try {

            String message;
            message = client.getIn().readLine();

            while (message != null) {
                System.out.println(message + "indi");
                char Indicator = message.charAt(0);

                String sender = message.substring(1, message.indexOf("@"));
                String realMessage = message.substring(message.indexOf("@") + 1);
                if (Indicator == '2') {
                    System.out.println("[ALL] " + sender + ": " + realMessage);
                } else if (Indicator == '3') {
                    System.out.println("[Private] " + sender + ": " + realMessage);
                } else if (Indicator == '4') {
                    //TODO 完成群组聊天功能
                    System.out.println("Function haven't finish");
                } else if (Indicator == '9') {
                    message = message.substring(1);
                    while (message.contains("@")) {
                        List<String> onlineUserList = new ArrayList<>();
                        onlineUserList.add(message.substring(0, message.indexOf("@")));
                        client.setOnlineUserList(onlineUserList);
                        message = message.substring(message.indexOf("@") + 1);
                        client.sendHearBeats();
                    }
                } else {
                    System.out.println("***Unidentified Message***");
                }
                message = client.getIn().readLine();
            }

        } catch (IOException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
