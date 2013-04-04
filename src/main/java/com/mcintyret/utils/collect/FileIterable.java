package com.mcintyret.utils.collect;

import java.io.*;
import java.util.Iterator;

/**
 * User: mcintyret2
 * Date: 31/03/2013
 */
public class FileIterable implements Iterable<String> {

    private final File file;

    public FileIterable(File file) {
        this.file = file;
    }

    public FileIterable(String filename) {
        this(new File(filename));
    }

    @Override
    public Iterator<String> iterator() {
        try {
            return new AbstractHasNextFetchingIterator<String>() {

                BufferedReader reader = new BufferedReader(new FileReader(file));

                @Override
                protected boolean doHasNext() {
                    try {
                        String line = reader.readLine();
                        setNext(line);
                        return line != null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                protected void doRemove(String removed) {

                }
            };
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
