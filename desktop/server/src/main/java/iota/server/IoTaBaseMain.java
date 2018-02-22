package iota.server;

import iota.common.Constants;
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
    private static boolean running = true;

    public static void main(String[] args) {
        startupParams = new StartupParams(args);
        defStore = new DefinitionStore();

        defStore.populateStore(startupParams.getDefLocation());


        sql = new SqlHandler(startupParams.getSqlUrl(), startupParams.getSqlUser(), startupParams.getSqlPass(), startupParams.getSchemaName());
        if (!sql.test()) {
            System.out.println("Oh noes scoob, SQL is borken.");
            System.exit(2);
        }
        sqlThread = new Thread(sql);
        sqlThread.start();
        sqlThread.setName("Sql Thread");
        if (sqlThread.isAlive()) {
            System.out.println("Sql Thread Alive at ID " + sqlThread.getId());
        }


        net = new NetworkHandler(startupParams.getAppPort());
        netThread = new Thread(net);
        netThread.start();
        netThread.setName("Network Thread");

        if (netThread.isAlive()) {
            System.out.println("Network Thread Alive at ID " + netThread.getId());
        }

        Scanner scan = new Scanner(System.in);
        while (running) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                System.out.println("Stopping...");
                net.requestStop();
                sql.requestStop();
                running = false;
            }
        }

    }

}
