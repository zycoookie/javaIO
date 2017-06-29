package me.cookie.nio.server;


import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.arch.Life;
import me.cookie.nio.group.IOEventListenerGroup;
import me.cookie.nio.group.ServerSocketGroup;
import me.cookie.nio.workflow.DefaultGroupWorkflow;
import me.cookie.nio.workflow.ServerContext;
import me.cookie.nio.workflow.Workflow;

import java.net.InetSocketAddress;

/**
 * Created by cookie on 2017/6/28.
 */
@Slf4j
public class DefaultServer implements Server {

    private ServerContext serverContext;

    private Life life = new Life();

    public DefaultServer(ServerContext context){
        this.serverContext = context;
    }

    @Override
    public void init() {
        if(serverContext == null){
            log.error("serverContext is null");
        }else{
            Workflow workflow = serverContext.getWorkflow();
            if(workflow == null){
                workflow = new DefaultGroupWorkflow(serverContext);
                workflow.addLast(new ServerSocketGroup());
                workflow.addLast(new IOEventListenerGroup());
            }
            //初始化workflow
            initWorkflow(workflow);
            life.init();
        }
    }

    void initWorkflow(Workflow workflow){
        workflow.init();
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
    public void setServerContext(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public ServerContext getServerContext() {
        return serverContext;
    }

    @Override
    public void bind(InetSocketAddress address) {

    }
}
