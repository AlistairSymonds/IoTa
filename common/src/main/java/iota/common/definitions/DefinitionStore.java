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
    private HashMap<Byte, IDatabaseDef> defs;

    public  DefinitionStore(){
        defs = new HashMap<Byte, IDatabaseDef>();
    }

    public int populateStore(Path folder, String filter){
        int defsAdded = 0;
        try {
            DirectoryStream<Path> dir = Files.newDirectoryStream(folder, filter);
            System.out.println("Searching for definitions in: "+folder.toString());
            for(Path file : dir){
                System.out.println("Found definition file: " + file.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defsAdded;

    }
}
