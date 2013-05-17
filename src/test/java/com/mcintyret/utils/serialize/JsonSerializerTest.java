package com.mcintyret.utils.serialize;

import org.testng.annotations.Test;

/**
 * User: mcintyret2
 * Date: 16/05/2013
 */

@Test
public class JsonSerializerTest extends AbstractSerializerTest {

    private final JsonSerializer serializer = new JsonSerializer();

    @Override
    protected Serializer getSerializer() {
        return serializer;
    }

    @Override
    protected String getFilename() {
        return ROOT + "jsonSerialization";
    }

}
