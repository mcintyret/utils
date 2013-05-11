package com.mcintyret.utils.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.mcintyret.utils.io.RuntimeIoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: mcintyret2
 * Date: 11/05/2013
 */
public class HessianSerializer extends AbstractOutputStreamSerializer {

    @Override
    public void serialize(Object obj, OutputStream os) {
        try {
            try (CloseableHessianOutputStream hos = new CloseableHessianOutputStream(os)) {
                hos.writeObject(obj);
            }
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        try {
            try (CloseableHessianInputStream his = new CloseableHessianInputStream(is)) {
                return clazz.cast(his.readObject());
            }
        } catch (IOException e) {
            throw new RuntimeIoException(e);
        }
    }

    @Override
    public String getSuffix() {
        return ".hessian";
    }

    private static class CloseableHessianOutputStream extends Hessian2Output implements AutoCloseable {

        public CloseableHessianOutputStream(OutputStream os) {
            super(os);
        }
    }

    private static class CloseableHessianInputStream extends Hessian2Input implements AutoCloseable {

        public CloseableHessianInputStream(InputStream is) {
            super(is);
        }
    }
}
