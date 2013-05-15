package com.mcintyret.utils.serialize;

import java.io.File;

/**
 * User: mcintyret2
 * Date: 13/05/2013
 */
public abstract class AbstractSerializer implements Serializer {

    private String preprocessFileName(String filename) {
        return filename.endsWith(getSuffix()) ? filename : filename + getSuffix();
    }

    public void serialize(Object obj, String filename) {
        serialize(obj, new File(preprocessFileName(filename)));
    }

    public <T> T deserialize(String filename, Class<T> clazz) {
        return deserialize(new File(preprocessFileName(filename)), clazz);
    }

}
