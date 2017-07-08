package iota.server;

import java.util.HashMap;

/**
 * Created by alist on 8/07/2017.
 */
public class StartupParams {


    private HashMap<String, String> p;

    public StartupParams (HashMap<String, String> args) throws IllegalArgumentException{
        this.p = args;
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
}
