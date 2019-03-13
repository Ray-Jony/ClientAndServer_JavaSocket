package Client;

public class ClientCheckServerActive extends Thread {
    private boolean tmpActive;
    private Client client;

    public ClientCheckServerActive(Client client) {
        this.client = client;
        this.tmpActive = client.isServerActive();
        System.out.println("检查服务器状态线程已启动");
        start();
    }

    @Override
    public void run() {
        while (tmpActive) {
            client.getOut().println("8");
            tmpActive = false;
//            System.out.println("检查服务器状态...");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("与服务器的连接已断开");
        client.setServerActive(false);
        System.out.println("输入任意内容以结束");
        try {
            Thread.sleep(3*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (client.isLogin()){

        }
    }

    public void setTmpActive(boolean tmpActive) {
        this.tmpActive = tmpActive;
    }
}
