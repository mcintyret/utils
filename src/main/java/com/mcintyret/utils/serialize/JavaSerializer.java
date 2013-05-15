package com.mcintyret.utils.serialize;

import com.mcintyret.utils.io.RuntimeIoException;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public class JavaSerializer extends AbstractStreamSerializer {

    private static final JavaSerializer INSTANCE = new JavaSerializer();

    public static Serializer getInstance() {
        return INSTANCE;
    }

    private JavaSerializer() {

    }

    @Override
    public void serialize(Object obj, OutputStream os) {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            return clazz.cast(ois.readObject());
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSuffix() {
        return ".ser";
    }
}
