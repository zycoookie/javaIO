package me.cookie.nio.server;

import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.workflow.ServerContext;

import java.net.InetSocketAddress;

/**
 * Created by cookie on 2017/6/28.
 */
public interface Server extends LifeCycle {

    void setServerContext(ServerContext serverContext);

    ServerContext getServerContext();

    void bind(InetSocketAddress address);
}
