package me.cookie.old.nio.request;

import me.cookie.arch.LifeCycle;
import me.cookie.old.nio.exception.NioException;

import java.nio.channels.Channel;

/**
 * Created by cookie on 2017/6/20.
 */
public interface Job extends LifeCycle {

    Object process(Channel channel) throws NioException;

}
