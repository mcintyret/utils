package com.mcintyret.utils.serialize;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 10/05/2013
 */
public interface Serializer {

    public void serialize(Object obj, String fileName);

    public void serialize(Object obj, File file);

    public void serialize(Object obj, Writer writer);

    public void serialize(Object obj, OutputStream os);

    public String serializeToString(Object obj);

    public byte[] serializeToBytes(Object obj);

    public <T> T deserializeFromString(String str, Class<T> clazz);

    public <T> T deserialize(String fileName, Class<T> clazz);

    public <T> T deserialize(File file, Class<T> clazz);

    public <T> T deserialize(InputStream is, Class<T> clazz);

    public <T> T deserialize(Reader reader, Class<T> clazz);

    public String getSuffix();

}
