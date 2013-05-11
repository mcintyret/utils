package com.mcintyret.utils;

import java.io.*;

/**
 * User: mcintyret2
 * Date: 12/04/2013
 */
public class FileUtils {

    public static void serialize(Object o, File file) {
        System.out.println("Serialising to " + file.getName());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(o);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void serialize(Object o, String filename) {
        serialize(o, new File(filename));
    }

    public static <T> T deserialize(File file, Class<T> clazz) {
        System.out.println("Deserialising data from " + file.getName());

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return clazz.cast(in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserialize(String filename, Class<T> clazz) {
        return deserialize(new File(filename), clazz);
    }

}
