package me.cookie.nio;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cookie on 2017/6/28.
 */
public abstract class Context {

    private Map<Object, Object> properties = new ConcurrentHashMap<>();

    public void setProperty(Object key, Object value){
        properties.put(key, value);
    }

    public Object getProperty(Object value){
        return properties.get(value);
    }

}
