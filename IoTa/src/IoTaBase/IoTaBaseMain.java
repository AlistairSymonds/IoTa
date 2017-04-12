package IoTaBase;

import java.sql.SQLException;

import IoTaBase.sql.SqlUpdateHandler;

public class IoTaBase {
  public static SqlUpdateHandler insert = null;
  public static NetworkHandler net = null;
  public static Thread insertThread = null;
  public static Thread netThread = null;

  public static void main(String[] args) {
    IoTaUtil.initDataDefs();

    String hostname = "";
    String user = "";
    String password = "";
    int portNum = 0;

    if (args.length == 4) {
      hostname = args[0];
      user = args[1];
      password = args[2];
      portNum = Integer.parseInt(args[3]);
    } else {
      System.out.println("java -jar IoTaBase.jar HOSTNAME SQLUSER SQLPASSWORD EXTERNAL_PORTNUMBER");
    }

    try {
      insert = new SqlUpdateHandler(hostname, user, password);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("SQL Connection Established");
    insertThread = new Thread(insert);
    insertThread.start();
    insertThread.setName("SqlInsert Thread");
    if (insertThread.isAlive()) {
      System.out.println("Insert Thread Alive at ID " + insertThread.getId());
    }

    net = new NetworkHandler(portNum);
    System.out.println("NetworkHandler Constructed");
    System.out.println("Creating Network Thread");
    netThread = new Thread(net);
    System.out.println("Starting Network Thread");
    netThread.start();
    netThread.setName("Network Thread");

    if (netThread.isAlive() == true) {
      System.out.println("Network Thread Alive at ID " + netThread.getId());
    }

  }

}
