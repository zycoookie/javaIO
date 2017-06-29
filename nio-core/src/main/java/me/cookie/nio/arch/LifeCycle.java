package me.cookie.nio.arch;

/**
 * Created by cookie on 2017/6/25.
 */
public interface LifeCycle {

    void init();

    boolean isInit();

    void start();

    boolean isStart();

    void finish();

    boolean isFinish();
}
