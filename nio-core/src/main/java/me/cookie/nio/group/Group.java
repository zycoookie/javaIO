package me.cookie.nio.group;

import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.handler.Chain;
import me.cookie.nio.worker.Worker;

import java.util.Collection;

/**
 * Created by cookie on 2017/6/25.
 */
public interface Group extends LifeCycle{

    void accept(Object obj);

    Collection<Worker> getWorkers();

    Chain createChain();
}
