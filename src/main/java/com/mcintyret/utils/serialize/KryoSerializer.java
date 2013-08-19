package com.mcintyret.utils.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.internal.UnsafeAllocator;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.objenesis.strategy.BaseInstantiatorStrategy;
import org.objenesis.strategy.InstantiatorStrategy;
import org.objenesis.strategy.SerializingInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: mcintyret2
 * Date: 19/08/2013
 */
public class KryoSerializer extends AbstractStreamSerializer {

    private final Kryo kryo = new Kryo();

    public KryoSerializer() {
        kryo.setInstantiatorStrategy(new GenericInstantiatorStrategy(kryo));
    }

    @Override
    public void serialize(Object obj, OutputStream os) {
        try (Output out = new Output(os)) {
            kryo.writeObject(out, obj);
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        try (Input in = new Input(is)) {
            return kryo.readObject(in, clazz);
        }
    }

    @Override
    public String getSuffix() {
        return SerializationStrategy.KRYO.getSuffix();
    }

    public Kryo configure() {
        return kryo;
    }

    private static class GenericInstantiatorStrategy extends BaseInstantiatorStrategy {

        private final Kryo kryo;

        private GenericInstantiatorStrategy(Kryo kryo) {
            this.kryo = kryo;
        }

        @Override
        public ObjectInstantiator newInstantiatorOf(Class aClass) {
            return new GenericObjectInstantiator(aClass, kryo);
        }

    }

    private static class UnsafeInstantiatorStrategy extends BaseInstantiatorStrategy {

        private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

        @Override
        public ObjectInstantiator newInstantiatorOf(final Class aClass) {
            return new ObjectInstantiator() {
                @Override
                public Object newInstance() {
                    try {
                        return UNSAFE_ALLOCATOR.newInstance(aClass);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        }
    }

    private static class GenericObjectInstantiator implements ObjectInstantiator {

        private static final InstantiatorStrategy SERIALIZING_INSTANTIATOR_STRATEGY = new SerializingInstantiatorStrategy();
        private static final InstantiatorStrategy STD_INSTANTIATOR_STRATEGY = new StdInstantiatorStrategy();
        private static final InstantiatorStrategy UNSAFE_INSTANTIATOR_STRATEGY = new UnsafeInstantiatorStrategy();

        private final ObjectInstantiator[] instantiators;

        private final Class<?> clazz;
        private final Kryo kryo;

        private GenericObjectInstantiator(Class<?> clazz, Kryo kryo) {
            this.clazz = clazz;
            this.kryo = kryo;
            instantiators = new ObjectInstantiator[]{
                new NewInstanceInstantiator(clazz),
//                new ConstructorInstantiator(clazz),
//                new AccessibleInstantiator(clazz),
                SERIALIZING_INSTANTIATOR_STRATEGY.newInstantiatorOf(clazz),
                STD_INSTANTIATOR_STRATEGY.newInstantiatorOf(clazz),
                UNSAFE_INSTANTIATOR_STRATEGY.newInstantiatorOf(clazz)
            };
        }

        @Override
        public Object newInstance() {
            RuntimeException lastThrown = null;
            for (ObjectInstantiator instantiator : instantiators) {
                try {
                    Object obj = instantiator.newInstance();
                    // Yay, it worked! Now register it!
                    kryo.register(clazz).setInstantiator(instantiator);
                    return obj;
                } catch (RuntimeException re) {
                    lastThrown = re;
                }
            }
            throw lastThrown;
        }
    }
}
