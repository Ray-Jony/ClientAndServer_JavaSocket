package Client;

/**
 * @author :
 * @version :
 */
public class ClientLogin {
    Client client;
    String loginInfo;
    String permission;

    public ClientLogin(Client client) {
        this.client = client;
        this.loginInfo = null;
        this.permission = "None";
    }


    public void start() {

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
//            client.getClientUI().successfulLogin();
            System.out.println("成功登陆");
            client.setUser(loginInfo.substring(0, loginInfo.indexOf("@")));
            client.setLogin(true);
            client.setReLoginInfo(loginInfo);
//            client.getClientUI().getFrame().dispose();

        } else {
//            client.getClientUI().loginFailed();
        }

    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }
}
