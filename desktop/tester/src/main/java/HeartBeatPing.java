import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HeartBeatPing {
    public static void main(String[] args) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        InetSocketAddress addr = new InetSocketAddress(hostName, portNumber);

        try (Socket devSocket = new Socket()) {
            devSocket.connect(addr, 1000);
            DataInputStream din = new DataInputStream(devSocket.getInputStream());
            DataOutputStream dout = new DataOutputStream(devSocket.getOutputStream());

            long pingStart = System.currentTimeMillis();
            byte[] hbeatbytes = {4, 0, 1, 60};
            dout.write(hbeatbytes);


            int len = din.read();
            byte buf[] = new byte[len];
            din.read(buf, 1, len - 1);
            long pingFinish = System.currentTimeMillis();

            for (int i = 0; i < buf.length; i++) {
                System.out.print(buf[i] + " ");
            }
            System.out.println();
            System.out.println("Ping to " + hostName + " was " + (pingFinish - pingStart) + " [ms]");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
