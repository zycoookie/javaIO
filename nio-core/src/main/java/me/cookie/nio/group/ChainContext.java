package me.cookie.nio.group;

import lombok.Getter;
import lombok.Setter;
import me.cookie.nio.Context;
import me.cookie.nio.handler.Chain;
import me.cookie.nio.workflow.ServerContext;

import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.util.LinkedList;

/**
 * Created by cookie on 2017/6/28.
 */
@Getter
@Setter
public class ChainContext extends Context {

    private ServerContext context;

    private Chain chain;

    private Channel channel;

    private Selector selector;

    private LinkedList<Object> tasks = new LinkedList<>();

    public ChainContext(ServerContext chainContext,Chain chain){
        this.context = chainContext;
        this.chain = chain;
    }

    public Chain getChain(){
        return this.chain;
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

    public void addTask(Object task){
        tasks.offer(task);
    }

    public Object takeTask(){
        return tasks.poll();
    }

}
