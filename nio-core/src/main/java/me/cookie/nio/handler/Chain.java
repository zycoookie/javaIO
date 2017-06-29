package me.cookie.nio.handler;

import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.group.ChainContext;
import me.cookie.nio.group.Group;

/**
 * Created by cookie on 2017/6/28.
 */
public interface Chain extends LifeCycle{

    void setChainContext(ChainContext chainContext);

    void addAll(Handler... handlers);

    void addHandler(Handler handler);

    void doChain(ChainContext context);

    Group getGroup();
}
