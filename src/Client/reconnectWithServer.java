package Client;

public class reconnectWithServer extends Thread {
    private Client client;

    public reconnectWithServer(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        super.run();
    }
}
