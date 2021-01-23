package io.conrado.api.starwars.utils;

import java.util.ArrayList;
import java.util.List;

public class IteratorsUtils {
    public static List<Object> toList(Iterable iterable) {
        List<Object> list = new ArrayList<Object>();
        
        for (Object obj : iterable)
            list.add(obj);
        
        return list;
    }
}
