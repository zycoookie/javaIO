package me.cookie.nio.handler;

import me.cookie.nio.Context;
import me.cookie.nio.arch.LifeCycle;

/**
 * Created by cookie on 2017/6/28.
 */
public interface Handler extends LifeCycle{

    void handle(Context context);
}
