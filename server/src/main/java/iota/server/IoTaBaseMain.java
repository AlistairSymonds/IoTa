package iota.server;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.server.sql.SqlUpdateHandler;



public class IoTaBaseMain {
  public static iota.server.sql.SqlUpdateHandler insert;
  public static NetworkHandler net;
  public static Thread insertThread;
  public static Thread netThread;
  public static final Logger logger = Logger.getLogger(Constants.ERROR_LOGGER, null);

  public static void main(String[] args) {
    IoTaUtil.initDataDefs();

    String hostname = "";
    String user = "";
    String password = "";
    int portNum = 0;

    if (args.length == 4 ) {
      hostname = args[0];
      user = args[1];
      password = args[2];
      portNum = Integer.parseInt(args[3]);
      
      try{
        insert = new SqlUpdateHandler(hostname, user, password);
      }catch(SQLException e){
        logger.log(Level.SEVERE, e.getMessage(), e);
      }
    } else {
      System.out.println("java -jar IoTaBase.jar HOSTNAME SQLUSER SQLPASSWORD EXTERNAL_PORTNUMBER");
      System.exit(1);
    }

    
    insertThread = new Thread(insert);
    insertThread.start();
    insertThread.setName("SqlInsert Thread");


    net = new NetworkHandler(portNum);
    netThread = new Thread(net);
    netThread.start();
    netThread.setName("Network Thread");

    if (netThread.isAlive() == true) {
      System.out.println("Network Thread Alive at ID " + netThread.getId());
    }

  }

}
