package io.conrado.api.starwars.utils;

import java.util.ArrayList;
import java.util.List;

public class IteratorsUtils {
    
    public static <T> List<T> toList(Iterable<T> iterable) {
        List<T> list = new ArrayList<T>();
        
        for (T obj : iterable)
            list.add(obj);
        
        return list;
    }
}
