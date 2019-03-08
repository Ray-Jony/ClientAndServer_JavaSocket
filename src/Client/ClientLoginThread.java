package Client;

import java.io.IOException;

/**
 * @author :
 * @version :
 */
public class ClientLoginThread extends Thread {
    Client client;
    String loginInfo;

    public ClientLoginThread(Client client, String loginInfo) {
        this.client = client;
        this.loginInfo = loginInfo;
        start();
    }

    @Override
    public void run() {

            client.getOut().println(loginInfo);

            try {
                String permission = client.getIn().readLine();
                if (permission.equals("1")) {
                    client.getClientUI().successfulLogin();
                    client.setUser(loginInfo.substring(0,loginInfo.indexOf("@")));
                    client.setLogin(true);
                    client.getClientUI().getFrame().dispose();

                } else {
                    client.getClientUI().loginFailed();
                }
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
                e1.printStackTrace();
            }
    }
}
