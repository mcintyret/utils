package com.mcintyret.utils.serialize;

import org.testng.annotations.Test;

/**
 * User: mcintyret2
 * Date: 16/05/2013
 */

@Test
public class HessianSerializerTest extends AbstractSerializerTest {

    @Override
    protected Serializer getSerializer() {
        return HessianSerializer.getInstance();
    }

    @Override
    protected String getFilename() {
        return ROOT + "hessianSerialization";
    }
}
