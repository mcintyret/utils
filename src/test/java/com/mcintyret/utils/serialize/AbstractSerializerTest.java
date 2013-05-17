package com.mcintyret.utils.serialize;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

/**
 * User: mcintyret2
 * Date: 16/05/2013
 */
public abstract class AbstractSerializerTest {

    protected static final String ROOT = System.getProperty("user.dir") + "/src/test/resources/";

    @AfterMethod
    public void afterMethod() {
        new File(getFilename() + getSuffix()).delete();
    }

    @DataProvider
    public Object[][] suffixes() {
        return new Object[][]{
            {getSuffix(), getSuffix()},
            {getSuffix(), null},
            {null, getSuffix()},
            {null, null}
        };
    }

    @Test(dataProvider = "suffixes")
    public void shouldSerializeAndDeserialize(String serSuffix, String deserSuffix) {
        Object test = getTestObject();

        String serFilename = serSuffix == null ? getFilename() : getFilename() + serSuffix;
        getSerializer().serialize(test, serFilename);

        String deserFilename = deserSuffix == null ? getFilename() : getFilename() + deserSuffix;
        Object result = getSerializer().deserialize(deserFilename, test.getClass());

        assertEquals(test, result);
    }

    @Test
    public void shouldSerializeToString() throws IOException {
        getSerializer().serialize(getTestObject(), getFilename());

        String asString = getSerializer().serializeToString(getTestObject());

        String fileContents = FileUtils.readFileToString(new File(getFilename() + getSuffix()));

        assertEquals(fileContents, asString);
    }

    @Test
    public void shouldSerializeToBytes() throws IOException {
        getSerializer().serialize(getTestObject(), getFilename());

        byte[] asBytes = getSerializer().serializeToBytes(getTestObject());

        byte[] fileContents = FileUtils.readFileToByteArray(new File(getFilename() + getSuffix()));

        assertArrayEquals(fileContents, asBytes);
    }

    protected abstract Serializer getSerializer();

    protected abstract String getFilename();

    protected Object getTestObject() {
        return new SerializationTest("a string", new double[]{1.1, 2.2, 3.3, 4.4});
    }

    private String getSuffix() {
        return getSerializer().getSuffix();
    }

    private static class SerializationTest implements Serializable {

        private String string;

        private double[] array;

        private SerializationTest(String string, double[] array) {
            this.string = string;
            this.array = array;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SerializationTest that = (SerializationTest) o;

            return Arrays.equals(array, that.array) && string.equals(that.string);

        }

        @Override
        public int hashCode() {
            int result = string.hashCode();
            result = 31 * result + Arrays.hashCode(array);
            return result;
        }
    }
}
