package me.cookie.old.nio.event;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.cookie.old.nio.Group;
import me.cookie.arch.LifeCycle;
import me.cookie.old.nio.ServerContext;
import me.cookie.old.nio.exception.ExceptionCode;
import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
public class SocketChannelEventListenerGroup implements Group, LifeCycle {


    private ExecutorService service;
    @Getter
    private volatile boolean init = false;
    @Getter
    private volatile boolean finish = false;

    private Selector selector;

    private Job job;

    @Setter
    @Getter
    private ServerContext serverContext;


    @Override
    public void receive(Object object) throws NioException {
        if(!isInit()){
            throw new NioException(ExceptionCode.NOT_INIT);
        }
        SocketChannel channel = (SocketChannel) object;
        try {
            channel.register(selector, SelectionKey.OP_READ);
        } catch (ClosedChannelException e) {
            log.error("channel register exception",e);
        }
        log.info("regist key number is "+selector.keys());
        try {
            job.process(channel);
        } catch (NioException e) {
            log.error("job process exception",e);
        }finally {
            //TODO 这里暂时不知道怎么处理
        }
    }

    @Override
    public void setJob(Job job) {

    }

    @Override
    public void init() {
        service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        try {
            selector = SelectorProvider.provider().openSelector();
            serverContext.setEventSelector(Collections.singleton(selector));
            this.job = new SocketEventListener(selector,this);
            this.job.init();
        } catch (IOException e) {
            log.error("selector open exception",e);
        }
        init = true;
    }

    @Override
    public void finish() {
        service.shutdown();
        finish = true;
    }
}
