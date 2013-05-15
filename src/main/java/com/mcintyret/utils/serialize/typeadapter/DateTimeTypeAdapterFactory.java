package com.mcintyret.utils.serialize.typeadapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;

/**
 * User: mcintyret2
 * Date: 13/05/2013
 */
public class DateTimeTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, TypeToken<T> type) {
        if (type.getRawType() == DateTime.class) {
            return (TypeAdapter<T>) new TypeAdapter<DateTime>() {

                @Override
                public void write(JsonWriter out, DateTime value) throws IOException {
                    gson.toJson(value.toDate(), Date.class, out);
                }

                @Override
                public DateTime read(JsonReader in) throws IOException {
                    return new DateTime(((Date) gson.fromJson(in, Date.class)).getTime());
                }
            };
        } else {
            return null;
        }
    }
}
