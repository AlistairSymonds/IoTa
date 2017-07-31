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

        Option sqlUserOpt = Option.builder("sqluser").desc("SQL Username").required().hasArg().build();
        opts.addOption(sqlUserOpt);

        Option sqlPassOpt = Option.builder("sqlpass").desc("SQL Password").required().hasArg().build();
        opts.addOption(sqlPassOpt);

        Option sqlUrlOpt = Option.builder("sqlurl").desc("SQL URL").required().hasArg().build();
        opts.addOption(sqlUrlOpt);

        Option defintionPathOpt = Option.builder("definitionpath").desc("File path to folder containing definition files").hasArg().build();
        opts.addOption(defintionPathOpt);

        Option appPortOpt = Option.builder("appport").desc("TCP Port on which the application will run, default is 2812").hasArg().build();
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
        int port = 2812;
        try {
            if (cmdLine.hasOption("appport")) {
                port = Integer.parseInt(cmdLine.getOptionValue("appport"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    public String getSqlUser() {
        return cmdLine.getOptionValue("sqluser");
    }

    public String getSqlPass() {
        return cmdLine.getOptionValue("sqlpass");
    }

    public String getSqlUrl() {
        System.out.println(cmdLine.hasOption("sqlurl"));
        System.out.println(cmdLine.getOptionValue("sqlurl"));
        return cmdLine.getOptionValue("sqlurl");
    }

    public Path getDefLocation() {
        Path p;
        p = Paths.get(".");
        try {
            if (cmdLine.hasOption("definitionpath")) {
                p = Paths.get(cmdLine.getOptionValue("definitionpath"));
            }
        } catch (Exception e) {

        }
        return p;
    }
}
