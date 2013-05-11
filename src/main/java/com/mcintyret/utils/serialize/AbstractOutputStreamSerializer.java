package com.mcintyret.utils.serialize;

import com.mcintyret.utils.io.RuntimeIoException;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public abstract class AbstractOutputStreamSerializer implements Serializer {

    @Override
    public void serialize(Object obj, String fileName) {
        try {
            serialize(obj, new BufferedOutputStream(new FileOutputStream(preprocessFileName(fileName))));
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public String serializeToString(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(obj, new BufferedOutputStream(baos));
        return baos.toString();
    }

    @Override
    public void serialize(Object obj, Writer writer) {
        throw new UnsupportedOperationException("This serializer must be used with Input/OutputStreams");
    }

    @Override
    public <T> T deserializeFromString(String str, Class<T> clazz) {
        return deserialize(new StringReader(str), clazz);
    }

    @Override
    public <T> T deserialize(String filename, Class<T> clazz) {
        try {
            return deserialize(new BufferedReader(new FileReader(preprocessFileName(filename))), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> clazz) {
        throw new UnsupportedOperationException("This serializer must be used with Input/OutputStreams");
    }


    private String preprocessFileName(String filename) {
        return filename.endsWith(getSuffix()) ? filename : filename + getSuffix();
    }
}
