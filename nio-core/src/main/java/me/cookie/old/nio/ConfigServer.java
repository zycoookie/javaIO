package me.cookie.old.nio;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.cookie.arch.LifeCycle;
import me.cookie.old.nio.event.SocketChannelEventListenerGroup;
import me.cookie.old.nio.request.Job;
import me.cookie.old.nio.worker.SocketChannelWorkerGroup;

/**
 * Created by cookie on 2017/6/20.
 */
@Getter
@Setter
@Slf4j
public abstract class ConfigServer implements LifeCycle {
    private ServerContext serverContext;
    private Job workJob;
    private SocketChannelEventListenerGroup eventListenerGroup;
    private SocketChannelWorkerGroup workerGroup;
    private int port;
    private boolean init = false;
    private boolean finish = false;

    @Override
    public void init(){
        serverContext = new ServerContext();
        serverContext.setEventGroup(eventListenerGroup);
        serverContext.setWorkGroup(workerGroup);
        eventListenerGroup.setServerContext(serverContext);
        workerGroup.setServerContext(serverContext);
        workerGroup.setJob(workJob);
        log.debug("configServer init");
    }

}
