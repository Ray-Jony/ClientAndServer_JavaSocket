package Client;

import java.io.IOException;

/**
 * @author :
 * @version :
 */
public class ClientLoginThread extends Thread {
    Client client;
    String loginInfo;
    String permission;

    public ClientLoginThread(Client client, String loginInfo) {
        this.client = client;
        this.loginInfo = loginInfo;
        this.permission = "None";
        start();
        System.out.println("登录线程已启动");
    }

    @Override
    public void run() {

        client.getOut().println(loginInfo);


        //等待接收来自ClientInThread的信息分发
        while (true) {
            if (permission.equals("1") || permission.equals("0"))
                break;
            else if (permission.equals("None")) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("登录线程接收到不明信息");
                break;
            }
        }

        if (permission.equals("1")) {
            client.getClientUI().successfulLogin();
            client.setUser(loginInfo.substring(0, loginInfo.indexOf("@")));
            client.setLogin(true);
            client.setReloginInfo(loginInfo);
            client.getClientUI().getFrame().dispose();

        } else {
            client.getClientUI().loginFailed();
        }

    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
