package iota.client.network;

public class ConcurrentConnectionStatus {
    private volatile ConnectionStatus status;

    public ConcurrentConnectionStatus(ConnectionStatus status) {
        this.status = status;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public synchronized ConnectionStatus setStatus(ConnectionStatus newStatus) {
        ConnectionStatus oldStatus = status;
        status = newStatus;
        //System.out.println("New status: " + status);
        this.notifyAll();
        return oldStatus;
    }
}
