package me.cookie.nio.group;

import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.Context;
import me.cookie.nio.arch.AbstractLife;
import me.cookie.nio.arch.Life;
import me.cookie.nio.arch.LifeCycle;
import me.cookie.nio.handler.Chain;
import me.cookie.nio.handler.DefaultHandleChain;
import me.cookie.nio.handler.Handler;
import me.cookie.nio.worker.HandleChainWorker;
import me.cookie.nio.worker.Worker;
import me.cookie.nio.worker.WorkerFactory;
import me.cookie.nio.workflow.ServerContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cookie on 2017/6/25.
 */
@Slf4j
public class ServerSocketGroup extends SocketBindGroup implements LifeCycle {

    private Life life = new Life();

    private Selector selector;

    private List<Handler> handlers;

    private ServerContext serverContext;

    public ServerSocketGroup(){
        super(null);
    }


    public ServerSocketGroup(ServerContext serverContext,WorkerFactory factory) {
        super(factory);
        this.serverContext = serverContext;
        this.workers = new LinkedHashSet<>();
    }

    @Override
    protected void doUnbind(SocketAddress address) {

    }

    @Override
    public void doBind(SocketAddress address) {
        Worker worker = getFactory().getWorker();
        worker.init();
        if(worker.isInit()){
            workers.add(worker);
        }else{
            log.error("bind exception, worker is not prepared");
        }
    }

    private static Selector getSelector(){
        try {
            return SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            log.error("selector cannot open",e);
            return null;
        }
    }

    @Override
    public void init() {
        if(getFactory() == null){
            setFactory(new ServerSocketWorkerFactory());
        }
        try {
            selector = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            log.error("selector open exception",e);
        }
        this.handlers = Collections.singletonList(new SocketBindHandler());
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
    public Chain createChain() {
        Chain chain = new DefaultHandleChain(this);
        ChainContext chainContext = new ChainContext(serverContext,chain);
        chain.setChainContext(chainContext);
        chain.addAll((Handler[]) this.handlers.toArray());
        return chain;
    }


    public class ServerSocketWorkerFactory implements WorkerFactory {
        @Override
        public Worker getWorker() {
            return new HandleChainWorker(ServerSocketGroup.this,createChain());
        }
    }


    public class SocketBindHandler extends AbstractLife implements Handler {
        @Override
        public void handle(Context context) {
            ChainContext chainContext = (ChainContext) context;
            if(selector != null && selector.isOpen()){
                try {
                    ServerSocketChannel channel = ServerSocketChannel.open();
                    chainContext.setChannel(channel);
                    Object taskObj = ((ChainContext) context).takeTask();
                    if(taskObj instanceof InetSocketAddress){
                        channel.bind((SocketAddress) taskObj);
                    }
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_ACCEPT);

                    selector.select(1000);

                    while(((ChainContext) context).getChain().isStart()){
                        Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                        if(selectionKeySet != null && !selectionKeySet.isEmpty()){
                            selectionKeySet.stream().forEach(selectionKey -> {
                                selectionKeySet.remove(selectionKey);
                                if(selectionKey.isAcceptable()){
                                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                                    try {
                                        SocketChannel socketChannel = serverSocketChannel.accept();
                                        chainContext.addTask(socketChannel);
                                        chainContext.getChain().doChain(chainContext);
                                    } catch (IOException e) {
                                        log.error("socketChannel accept exception",e);
                                    }catch (Exception e){
                                        log.error("doChain exception");
                                    }
                                }

                            });
                        }
                    }
                } catch (IOException e) {
                    log.error("channel open exception",e);
                }
            }
        }
    }
}
