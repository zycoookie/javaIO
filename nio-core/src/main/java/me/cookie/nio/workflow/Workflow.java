package me.cookie.nio.workflow;

import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.group.Group;

/**
 * Created by cookie on 2017/6/26.
 */
public interface Workflow extends LifeCycle {

    Group next(Group group);

    void addLast(Group group);

    ServerContext getSeverContext();
}
