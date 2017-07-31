package iota.server;

import iota.common.Constants;
import iota.common.IoTaUtil;
import iota.common.definitions.DefinitionStore;
import iota.server.network.NetworkHandler;
import iota.server.sql.SqlHandler;

import java.util.Scanner;
import java.util.logging.Logger;



public class IoTaBaseMain {
  public static SqlHandler sql;
  public static NetworkHandler net;
  public static Thread sqlThread;
  public static Thread netThread;
  public static DefinitionStore defStore;
  public static StartupParams startupParams;
  public static final Logger logger = Logger.getLogger(Constants.ERROR_LOGGER, null);

  public static void main(String[] args) {
    startupParams = new StartupParams(args);
    defStore = new DefinitionStore();

    defStore.populateStore(startupParams.getDefLocation());



    sql = new SqlHandler(startupParams.getSqlUrl(), startupParams.getSqlUser(), startupParams.getSqlPass());
    sqlThread = new Thread(sql);
    sqlThread.start();
    sqlThread.setName("Sql Thread");
    if (sqlThread.isAlive() == true) {
      System.out.println("Sql Thread Alive at ID " + sqlThread.getId());
    }



    net = new NetworkHandler(startupParams.getAppPort());
    netThread = new Thread(net);
    netThread.start();
    netThread.setName("Network Thread");

    if (netThread.isAlive() == true) {
      System.out.println("Network Thread Alive at ID " + netThread.getId());
    }

    Scanner scan = new Scanner(System.in);
    while (true) {
      if (scan.hasNext()) {
        if (scan.next().equalsIgnoreCase("stop")) {
          System.out.println("Stopping...");
          net.stop = true;
          sql.stop = true;
        }
      }
    }

  }

}
