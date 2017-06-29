package me.cookie.nio.handler;

import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.arch.Life;
import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.group.ChainContext;
import me.cookie.nio.group.Group;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cookie on 2017/6/29.
 */
@Slf4j
public class DefaultHandleChain implements Chain,LifeCycle {

    private List<Handler> handlers = new LinkedList<>();

    private Life life = new Life();

    private int index = 0;

    private ChainContext context;

    private Group group;

    public DefaultHandleChain(Group group){
        this.group = group;
    }

    @Override
    public void init() {
        handlers.stream().forEach(handler -> handler.init());

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
    public void setChainContext(ChainContext chainContext) {
        this.context = chainContext;
    }

    @Override
    public void addAll(Handler... handlerArr) {
        handlers.addAll(Arrays.asList(handlerArr));
    }

    @Override
    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    @Override
    public void doChain(ChainContext context) {
        if(index == handlers.size()){
            Object task = context.takeTask();
            if (task == null) {
                log.error("no task can accept");
            }else{
                getGroup().accept(task);
            }
        }else{
            handlers.get(index).handle(context);
            index++;
        }
    }

    @Override
    public Group getGroup() {
        return group;
    }
}
