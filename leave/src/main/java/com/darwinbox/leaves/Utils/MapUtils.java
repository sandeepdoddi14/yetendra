package com.darwinbox.leaves.Utils;

import java.util.HashMap;

public class MapUtils {

    public  HashMap<Object, Object> reverseHashMap(HashMap map) {
        HashMap<Object, Object> reversedHashMap = new HashMap<Object, Object>();

        for (Object key : map.keySet()) {
            reversedHashMap.put(map.get(key), key);

        }
        return reversedHashMap;
    }

    public  String getValueOfReversedMap(HashMap<String, String> map, String key) {
        HashMap reversed=reverseHashMap(map);
        try {
            if (!key.equalsIgnoreCase("empty")) {
                return reversed.get(key).toString();
            }
            return "empty";
        }catch (NullPointerException e){
            System.out.println("Null Pointer"+key);
            System.out.println("Null Pointer"+map.values());
            return key;
        }
    }

    public String notContainsInMap(HashMap map, String search) {
        HashMap<Object, Object> reversedHashMap = new HashMap<Object, Object>();
        for (Object key : map.keySet()) {
            reversedHashMap.put(map.get(key), key);
        }

        for (Object key : reversedHashMap.keySet()) {
            if (!key.toString().contains(search)) {
                return key.toString();
            }
        }
        return null;
    }

    public String searchMap(HashMap map, String serach) {
        HashMap<Object, Object> reversedHashMap = new HashMap<Object, Object>();
        for (Object key : map.keySet()) {
            reversedHashMap.put(map.get(key), key);
        }

        if (reversedHashMap.get(serach) == null)
            return null;

        return reversedHashMap.get(serach).toString();
    }

}
