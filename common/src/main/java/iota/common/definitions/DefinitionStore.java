package iota.common.definitions;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by alist on 8/07/2017.
 */
public class DefinitionStore{
    private HashMap<Byte, IFuncDef> defs;

    public  DefinitionStore(){
        defs = new HashMap<Byte, IFuncDef>();
    }

    public int populateStore(Path folder, String globFilter) {
        int defsAdded = 0;
        System.out.println("Searching for definitions in: " + folder.toString());

        try (DirectoryStream<Path> dir = Files.newDirectoryStream(folder, globFilter)) {
            for(Path file : dir){
                if (file.toFile().isFile()) {
                    System.out.println("Found definition file: " + file.getFileName());
                    createParser(file).parseFile();

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return defsAdded;
    }

    public int populateStore(Path folder) {
        return populateStore(folder, "*");
    }

    private IFuncDefFileParser createParser(Path file) {
        if (file.endsWith(".txt")) {
            return new TxtParser(file);
        }

        //default case, try to parse as text
        return new TxtParser(file);
    }

}
