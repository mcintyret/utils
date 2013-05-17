package com.mcintyret.utils.serialize;

import org.testng.annotations.Test;

/**
 * User: mcintyret2
 * Date: 16/05/2013
 */

@Test
public class JavaSerializerTest extends AbstractSerializerTest {

    @Override
    protected Serializer getSerializer() {
        return JavaSerializer.getInstance();
    }

    @Override
    protected String getFilename() {
        return ROOT + "javaSerialization";
    }

}
