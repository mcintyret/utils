package com.mcintyret.utils.serialize;

import com.mcintyret.utils.io.RuntimeIoException;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public abstract class AbstractStreamSerializer extends AbstractSerializer {

    @Override
    public void serialize(Object obj, File file) {
        try {
            serialize(obj, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public String serializeToString(Object obj) {
        return serializeToByteArrayOutputStream(obj).toString();
    }

    @Override
    public byte[] serializeToBytes(Object obj) {
        return serializeToByteArrayOutputStream(obj).toByteArray();
    }

    private ByteArrayOutputStream serializeToByteArrayOutputStream(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(obj, new BufferedOutputStream(baos));
        return baos;
    }

    @Override
    public void serialize(Object obj, Writer writer) {
        throw new UnsupportedOperationException("This serializer must be used with Input/OutputStreams");
    }

    @Override
    public <T> T deserializeFromString(String str, Class<T> clazz) {
        return deserialize(IOUtils.toInputStream(str), clazz);
    }

    @Override
    public <T> T deserialize(File file, Class<T> clazz) {
        try {
            return deserialize(new BufferedInputStream(new FileInputStream(file)), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> clazz) {
        throw new UnsupportedOperationException("This serializer must be used with Input/OutputStreams");
    }

}
