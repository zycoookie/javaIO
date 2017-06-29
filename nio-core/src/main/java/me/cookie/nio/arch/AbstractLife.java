package me.cookie.nio.arch;

/**
 * Created by cookie on 2017/6/29.
 */
public class AbstractLife implements LifeCycle{
    Life life = new Life();
    @Override
    public void init() {
        life.init();
    }

    @Override
    public boolean isInit() {
        return life.isInit();
    }

    @Override
    public void start() {
        life.start();
    }

    @Override
    public boolean isStart() {
        return life.isStart();
    }

    @Override
    public void finish() {
        life.finish();
    }

    @Override
    public boolean isFinish() {
        return life.isFinish();
    }
}
