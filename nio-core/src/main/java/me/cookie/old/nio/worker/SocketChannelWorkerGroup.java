package me.cookie.old.nio.worker;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.cookie.old.nio.ServerContext;
import me.cookie.old.nio.exception.ExceptionCode;
import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
@Getter
@Setter
public class SocketChannelWorkerGroup extends WorkerGroup{
    private Job job;

    private ExecutorService service;

    private ServerContext serverContext;

    private boolean init = false;

    private boolean finish = false;


    @Override
    public void receive(Object object) throws NioException {
        if(!isInit()){
            throw new NioException(ExceptionCode.NOT_INIT);
        }
        SocketChannel socketChannel = (SocketChannel) object;
        service.execute(() -> {
            try {
                job.process(socketChannel);
            } catch (NioException e) {
                log.error("job execute exception",e);
            }
        });
    }

    @Override
    public void init() {
        service = new ThreadPoolExecutor(16, 16, 0, TimeUnit.SECONDS, new LinkedBlockingQueue());
        init = true;
    }

    @Override
    public void finish() {
        finish = true;
    }
}
