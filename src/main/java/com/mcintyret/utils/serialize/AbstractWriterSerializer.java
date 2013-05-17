package com.mcintyret.utils.serialize;

import com.mcintyret.utils.io.RuntimeIoException;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public abstract class AbstractWriterSerializer extends AbstractSerializer {

    @Override
    public void serialize(Object obj, File file) {
        try {
            serialize(obj, new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public String serializeToString(Object obj) {
        StringWriter writer = new StringWriter();
        serialize(obj, writer);
        return writer.toString();
    }

    @Override
    public byte[] serializeToBytes(Object obj) {
        return serializeToString(obj).getBytes();
    }

    @Override
    public void serialize(Object obj, OutputStream os) {
        serialize(obj, new OutputStreamWriter(os));
    }

    @Override
    public <T> T deserializeFromString(String str, Class<T> clazz) {
        return deserialize(new StringReader(str), clazz);
    }

    @Override
    public <T> T deserialize(File file, Class<T> clazz) {
        try {
            return deserialize(new FileReader(file), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        return deserialize(new InputStreamReader(is), clazz);
    }
}
