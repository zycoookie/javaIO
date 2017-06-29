package me.cookie.nio.worker;

import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.Context;
import me.cookie.nio.arch.Life;
import me.cookie.nio.group.ChainContext;
import me.cookie.nio.group.Group;
import me.cookie.nio.handler.Chain;

/**
 * Created by cookie on 2017/6/29.
 */
@Slf4j
public class HandleChainWorker implements Worker {

    private final Group group;

    private Chain chain;

    private Life life = new Life();

    public HandleChainWorker(Group group,Chain chain){
        this.group = group;
        this.chain = chain;
    }

    @Override
    public Group getGroup(){
        return group;
    }

    public Chain getChain(){
        return chain;
    }

    @Override
    public void doWork(Context context) {
        if(isInit()){
            Chain chain = getChain();
            chain.doChain((ChainContext) context);
        }else{
            log.error("chain is null");
        }
    }

    @Override
    public void init() {
        if(chain == null){
            chain = getGroup().createChain();
        }
        chain.init();
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
