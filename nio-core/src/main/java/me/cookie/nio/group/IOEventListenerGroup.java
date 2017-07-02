package me.cookie.nio.group;

import lombok.extern.slf4j.Slf4j;
import me.cookie.nio.Context;
import me.cookie.nio.arch.AbstractLife;
import me.cookie.nio.arch.Life;
import me.cookie.nio.handler.Chain;
import me.cookie.nio.handler.DefaultHandleChain;
import me.cookie.nio.handler.Handler;
import me.cookie.nio.worker.HandleChainWorker;
import me.cookie.nio.worker.Worker;
import me.cookie.nio.worker.WorkerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * Created by cookie on 2017/6/25.
 */
@Slf4j
public class IOEventListenerGroup extends AbstractGroup {

    private WorkerFactory factory;

    private Life life = new Life();

    public IOEventListenerGroup() {
        super(null);
    }

    private Selector readSelector;

    public IOEventListenerGroup(WorkerFactory factory){
        super(factory);
    }

    @Override
    public void init() {
        if(factory == null){
            this.factory = new SingletonIOEventListenerWorkerFactory();
        }
        try {
            readSelector = SelectorProvider.provider().openSelector();
        } catch (IOException e) {
            log.error("readSelector open exception",e);
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

        private byte[] readBytes(SocketChannel channel){

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            return null;
        }

        @Override
        public void handle(Context context) {
            ChainContext chainContext = (ChainContext) context;
            SocketChannel socketChannel = (SocketChannel) chainContext.takeTask();
            if(socketChannel == null){
                return;
            }
            if(socketChannel.isConnected()){
                try {
                    socketChannel.configureBlocking(false);
                } catch (IOException e) {
                    log.error("socketChannel configureBlocking exception",e);
                    return;
                }

                try {
                    socketChannel.register(chainContext.getSelector(), SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    log.error("socketChannel register exception",e);
                    return;
                }
                if(socketChannel.isRegistered()){
                    while(chainContext.getChain().isStart()){
                        Selector selector = chainContext.getSelector();
                        try {
                            selector.select(1000);

                            Set<SelectionKey> selectionKeys = selector.selectedKeys();

                            selectionKeys.stream().forEach(selectionKey -> {
                                if(selectionKey.isReadable()){
                                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
