
import java.io.IOException;
import java.sql.SQLOutput;


public class ClientInThread extends Thread {
    private Client client;

    public ClientInThread(Client client) {
        this.client = client;
        System.out.println("ClientInThread Created");
        start();
    }

    @Override
    public void run() {
        //思考：每个socket都有一个独特的outputStream 所以需要新的判断，来分配readline的内容, 避免调用outputStream
        try {

            String message = client.getIn().readLine();
//            System.out.println(message);

            while (message != null) {
                if (message.charAt(0) == '9') {

                    continue;
                }
                String sender = message.substring(1, message.indexOf("@"));
                String realMessage = message.substring(message.indexOf("@") + 1);
                if (message.charAt(0) == '2') {
                    System.out.println("[ALL] " + sender + ": " + realMessage);
                } else if (message.charAt(0) == '3') {
                    System.out.println("[Private] " + sender + ": " + realMessage);
                } else if (message.charAt(0) == '4') {
                    //TODO 完成群组聊天功能
                    System.out.println("Function haven't finish");
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
