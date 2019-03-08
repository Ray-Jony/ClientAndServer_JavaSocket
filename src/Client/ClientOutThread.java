package Client;

public class ClientOutThread extends Thread {
    private Client client;

    public ClientOutThread(Client client) {
        this.client = client;
        System.out.println("Client.ClientOutThread Created");
        start();
    }

    @Override
    public void run() {

        while (true) {
            System.out.println("Please chose what kind of message you want to send");
            System.out.printf("1.send to all %n2.send to someone%n3.send to a group%n");
            String in = client.getScanner().nextLine();
            if (in.equals("1")) {
                System.out.println("Type in the message you want to send to All:");
                String message = client.getScanner().nextLine();
                client.sendToAll(message);
            } else if (in.equals("2")) {
                System.out.println("who do you want to send to?");
//                System.out.println(client.getOnlineUserList());
                String targetUser = client.getScanner().nextLine();
                if (true) {//TODO 添加判断 targetUser是否在列表中。
                    System.out.println("Type in the message you want to send to " + targetUser + ":");
                    String message = client.getScanner().nextLine();
                    client.sendToSingle(targetUser, message);
                }
            } else if (in.equals("3")) {
                //TODO 群组功能待完善
            } else {
                System.out.println("Type in unknown message");
            }
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }


}
