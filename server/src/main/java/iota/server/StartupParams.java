package iota.server;


import org.apache.commons.cli.*;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by alist on 8/07/2017.
 */
public class StartupParams {

    private CommandLine cmdLine;


    public StartupParams(String[] args) throws IllegalArgumentException {


        CommandLineParser parser = new DefaultParser();
        Options opts = new Options();

        Option sqlUserOpt = Option.builder("sqluser").desc("SQL Username").required().build();
        opts.addOption(sqlUserOpt);

        Option sqlPassOpt = Option.builder("sqlpass").desc("SQL Password").required().build();
        opts.addOption(sqlPassOpt);

        Option sqlUrlOpt = Option.builder("sqlurl").desc("SQL URL").required().build();
        opts.addOption(sqlUrlOpt);

        Option defintionPathOpt = Option.builder("definitionpath").desc("File path to folder containing definition files").required().build();
        opts.addOption(defintionPathOpt);


        HelpFormatter formatter = new HelpFormatter();
        try {
            cmdLine = parser.parse(opts, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("IotaBase", opts);
            System.exit(1);
        }


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
        return Integer.parseInt(cmdLine.getOptionValue("appport"));
    }

    public String getSqlUser() {
        return cmdLine.getOptionValue("sqluser");
    }

    public String getSqlPass() {
        return cmdLine.getOptionValue("sqlpass");
    }

    public String getSqlUrl() {
        return cmdLine.getOptionValue("sqlurl");
    }

    public Path getDefLocation() {
        return Paths.get(cmdLine.getOptionValue("definitionpath"));
    }
}
