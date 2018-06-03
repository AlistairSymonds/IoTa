package iota.common.definitions;

import iota.common.functions.IFunction;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alist on 8/07/2017.
 */
public class DefinitionStore implements Iterable<IFunction> {
    private HashMap<Short, IFunction> defs;

    public DefinitionStore() {
        defs = new HashMap<Short, IFunction>();

    }


    /**
     * @param folder
     * @param globFilter
     * @return
     */
    public int populateStore(Path folder, String globFilter) {
        int defsAdded = 0;
        System.out.println("Searching for definitions in: " + folder.toString());

        try (DirectoryStream<Path> dir = Files.newDirectoryStream(folder, globFilter)) {
            for (Path file : dir) {
                if (file.toFile().isFile()) {
                    System.out.println("Found definition file: " + file.getFileName());
                    //IFunction newDef = createParser(file).parseFile();
                    //defs.put(newDef.IFunction(), newDef);
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

    @Override
    public Iterator<IFunction> iterator() {
        return new DefIterator();
    }

    class DefIterator implements Iterator<IFunction> {
        private List<IFunction> data;
        private int currentIndex;

        DefIterator() {
            currentIndex = 0;
            data = new ArrayList<IFunction>();
            data.addAll(defs.values());
        }

        @Override
        public boolean hasNext() {
            return currentIndex != data.size();
        }

        @Override
        public IFunction next() {
            IFunction retVal = data.get(currentIndex);
            currentIndex++;
            return retVal;
        }
    }
}
