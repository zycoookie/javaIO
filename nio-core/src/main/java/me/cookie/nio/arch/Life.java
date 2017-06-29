package me.cookie.nio.arch;

/**
 * Created by cookie on 2017/6/28.
 */
public class Life implements LifeCycle{

    public static final int NEW = 0;
    public static final int INIT = 1;
    public static final int START = 1 << 1;
    public static final int FINISH = 1 << 2;

    private volatile int position = NEW;

    @Override
    public void init() {
        synchronized (this){
            if(position == NEW){
                position = INIT;
            }
        }
    }

    public boolean isInit(){
        return position == INIT;
    }

    @Override
    public void start() {
        synchronized (this){
            if(position == INIT){
                position = START;
            }
        }
    }

    public boolean isStart(){
        return position == START;
    }

    @Override
    public void finish() {
        synchronized (this){
            position = FINISH;
        }
    }

    public boolean isFinish(){
        return position == FINISH;
    }


}
