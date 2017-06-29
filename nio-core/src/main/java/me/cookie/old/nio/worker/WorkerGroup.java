package me.cookie.old.nio.worker;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.cookie.old.nio.Group;
import me.cookie.arch.LifeCycle;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
@Getter
public abstract class WorkerGroup implements Group, LifeCycle {
}
