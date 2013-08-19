package com.mcintyret.utils.serialize;

import org.testng.annotations.Test;

/**
 * User: mcintyret2
 * Date: 19/08/2013
 */

@Test
public class KryoSerializerTest extends AbstractSerializerTest {

    private final Serializer serializer = new KryoSerializer();

    @Override
    protected Serializer getSerializer() {
        return serializer;
    }

    @Override
    protected String getFilename() {
        return ROOT + "kyroSerialization";
    }
}
