package iota.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import iota.common.IoTaUtil;

public class TcpDecoder implements Runnable {

  private Thread t;

  private Socket socket;

  public TcpDecoder(Socket socketIn) {
    socket = socketIn;
  }

  public void run() {
    try {
      DataInputStream in = new DataInputStream(socket.getInputStream());
      while (in.available() > 0 && socket.isConnected()) {
        int datalen;
        datalen = in.read();
        byte[] msg = new byte[datalen];
        msg[0] = (byte) datalen;
        in.read(msg, 1, datalen - 1);
        long devId = 0;

        // general structure
        // byte[0] = length (including itself)
        // byte[1 to 8] = device id
        // byte[n] = data type
        // byte[n+1 to n+5] = data value

        devId = IoTaUtil.bytes2Long(Arrays.copyOfRange(msg, 1, 9));
        for (int i = 9; i < datalen; i = i + 5) {
          int dataId = msg[i];
          int data = IoTaUtil.bytes2Int(Arrays.copyOfRange(msg, i + 1, i + 5));
          String query = IoTaUtil.getInsertQ(dataId, devId, data);
          IoTaBaseMain.insert.addUpdate(query);
        }
      }
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
