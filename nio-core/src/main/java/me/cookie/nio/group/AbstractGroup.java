package me.cookie.nio.group;

import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.worker.Worker;
import me.cookie.nio.worker.WorkerFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by cookie on 2017/6/25.
 */
public abstract class AbstractGroup implements Group , LifeCycle{

    protected Set<Worker> workers;

    private WorkerFactory factory;

    public WorkerFactory getFactory() {
        return factory;
    }

    public void setFactory(WorkerFactory factory) {
        this.factory = factory;
    }

    AbstractGroup(WorkerFactory factory){
        this.factory = factory;
    }

    public void setWorkers(WorkerFactory factory ,Set<Worker> workers) {
        this.workers = workers;
        this.factory = factory;
    }

    public Set<Worker> getWorkers() {
        return Collections.unmodifiableSet(workers);
    }
}
