//package com.mcintyret.utils.serialize.typeadapter;
//
//import com.google.common.collect.Multiset;
//import com.google.gson.Gson;
//import com.google.gson.TypeAdapter;
//import com.google.gson.TypeAdapterFactory;
//import com.google.gson.reflect.TypeToken;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//
///**
// * User: mcintyret2
// * Date: 13/05/2013
// */
//public class MultisetAdaptorFactory implements TypeAdapterFactory {
//    @Override
//    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
//        if (Multiset.class.isAssignableFrom(type.getRawType())) {
//            return new TypeAdapter<Multiset>() {
//                @Override
//                public void write(JsonWriter out, Multiset value) throws IOException {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//
//                @Override
//                public Multiset read(JsonReader in) throws IOException {
//                    return null;  //To change body of implemented methods use File | Settings | File Templates.
//                }
//            }
//        } else {
//            return null;
//        }
//    }
//
//    private <E> TypeAdapter<Multiset<E>> newMultisetTypeAdapter(TypeAdapter<E> elementAdapter) {
//        return new TypeAdapter<Multiset<E>>() {
//            @Override
//            public void write(JsonWriter out, Multiset<E> value) throws IOException {
//                out.beginObject();
//                for (E key : value.elementSet()) {
//                    out
//                }
//            }
//
//            @Override
//            public Multiset<E> read(JsonReader in) throws IOException {
//                return null;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }
//    }
//}
