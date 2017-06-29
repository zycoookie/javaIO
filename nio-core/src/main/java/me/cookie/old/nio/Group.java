package me.cookie.old.nio;

import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;

/**
 * Created by cookie on 2017/6/20.
 */
public interface Group {

    void receive(Object object) throws NioException;

    void setJob(Job job);
}
