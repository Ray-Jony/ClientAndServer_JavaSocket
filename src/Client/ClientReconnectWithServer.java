package Client;

public class ClientReconnectWithServer extends Thread {
    private Client client;

    public ClientReconnectWithServer(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        super.run();
    }
}
