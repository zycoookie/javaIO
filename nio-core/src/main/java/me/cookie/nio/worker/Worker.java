package me.cookie.nio.worker;

import me.cookie.nio.Context;
import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.group.Group;

/**
 * Created by cookie on 2017/6/25.
 */
public interface Worker extends LifeCycle{

    Group getGroup();

    void doWork(Context context);
}
