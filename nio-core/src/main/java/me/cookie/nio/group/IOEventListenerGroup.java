package me.cookie.nio.group;

import me.cookie.nio.Context;
import me.cookie.nio.arch.AbstractLife;
import me.cookie.nio.arch.Life;
import me.cookie.nio.handler.Chain;
import me.cookie.nio.handler.DefaultHandleChain;
import me.cookie.nio.handler.Handler;
import me.cookie.nio.worker.HandleChainWorker;
import me.cookie.nio.worker.Worker;
import me.cookie.nio.worker.WorkerFactory;

import java.nio.channels.SelectionKey;

/**
 * Created by cookie on 2017/6/25.
 */
public class IOEventListenerGroup extends AbstractGroup {

    private WorkerFactory factory;

    private Life life = new Life();

    public IOEventListenerGroup() {
        super(null);
    }

    public IOEventListenerGroup(WorkerFactory factory){
        super(factory);
    }

    @Override
    public void init() {
        if(factory == null){
            this.factory = new SingletonIOEventListenerWorkerFactory();
        }
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

    @Override
    public void accept(Object obj) {

    }

    @Override
    public Chain createChain() {
        return new DefaultHandleChain(this);
    }

    private class SingletonIOEventListenerWorkerFactory implements WorkerFactory{

        private Worker worker;

        @Override
        public Worker getWorker() {
            synchronized (SingletonIOEventListenerWorkerFactory.class){
                if(worker == null){
                    worker = new HandleChainWorker(IOEventListenerGroup.this,createChain());
                }
                return worker;
            }
        }
    }


    public class IOEventListenerWorker extends AbstractLife implements Handler{

        @Override
        public void handle(Context context) {
            ChainContext chainContext = (ChainContext) context;
            SelectionKey selectionKey = (SelectionKey) chainContext.takeTask();
            if(selectionKey == null){

            }
        }
    }

}
