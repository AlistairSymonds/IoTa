package iota.server;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by alist on 8/07/2017.
 */
public class StartupParams {


    private HashMap<String, String> p;

    public StartupParams (HashMap<String, String> args) throws IllegalArgumentException{
        this.p = args;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("SQL Url: ");
        s.append(getSqlUrl());
        s.append("\n");

        s.append("SQL User: ");
        s.append(getSqlUser());
        s.append("\n");

        s.append("SQL Pass: ");
        s.append(getSqlPass());
        s.append("\n");

        s.append("Port: ");
        s.append(getAppPort());
        s.append("\n");
        return s.toString();
    }

    public int getAppPort() {
        return Integer.parseInt(p.get("port"));
    }

    public String getSqlUser() {
        return p.get("sqluser");
    }

    public String getSqlPass() {
        return p.get("sqlpass");
    }

    public String getSqlUrl() {
        return p.get("sqlurl");
    }

    public Path getDefLocation() {
        if (p.get("defloc") != null) {
            return Paths.get(p.get("defloc"));
        } else {
            try {
                return Paths.get(IoTaBaseMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            } catch (URISyntaxException e) {

                e.printStackTrace();
                return null;
            }
        }

    }
}
