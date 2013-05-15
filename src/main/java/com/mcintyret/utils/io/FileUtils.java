package com.mcintyret.utils.io;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;

import static com.mcintyret.utils.collect.MoreIterables.toCollection;

/**
 * User: mcintyret2
 * Date: 12/04/2013
 */
public final class FileUtils {

    private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("hh:mm:ss-dd-MM-yyyy");

    public static void writeLines(String filename, Iterable<String> lines, WriteStrategy writeStrategy) throws IOException {
        writeLines(new File(filename), lines, writeStrategy);
    }

    public static void writeLines(File file, Iterable<String> lines, WriteStrategy writeStrategy) throws IOException {
        switch (writeStrategy) {
            case OVERWRITE:
                org.apache.commons.io.FileUtils.writeLines(file, toCollection(lines), false);
                break;
            case APPEND:
                org.apache.commons.io.FileUtils.writeLines(file, toCollection(lines), true);
                break;
            case TIMESTAMP_PREVIOUS:
                if (file.exists()) {
                    timestampFile(file);
                }
                org.apache.commons.io.FileUtils.writeLines(file, toCollection(lines));
                break;
        }
    }

    public static List<String> readLines(String filename) throws IOException {
        return readLines(new File(filename));
    }

    public static List<String> readLines(File file) throws IOException {
        return org.apache.commons.io.FileUtils.readLines(file);
    }

    public static void timestampFile(String filename) throws IOException {
        timestampFile(new File(filename));
    }

    public static void timestampFile(File file) throws IOException {
        if (file.exists()) {
            String newName = makeTimestampedFilename(file);
            if (!file.renameTo(new File(newName))) {
                throw new FileSystemException("Failed to rename file " + file + " to " + newName);
            }
        } else {
            throw new FileNotFoundException("No such file: " + file);
        }
    }

    private static String makeTimestampedFilename(File file) {
        String newFileName;
        String oldFileName = file.getAbsolutePath();
        String timestamp = '-' + FORMAT.print(DateTime.now());
        int lastDot = oldFileName.lastIndexOf('.');
        int lastSeparator = oldFileName.lastIndexOf(File.separatorChar);
        if (lastDot > lastSeparator) {
            newFileName = oldFileName.substring(0, lastDot) + timestamp + oldFileName.substring(lastDot);
        } else {
            newFileName = oldFileName + timestamp;
        }
        return newFileName;
    }


}
