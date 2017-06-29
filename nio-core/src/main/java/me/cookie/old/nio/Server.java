package me.cookie.old.nio;

import lombok.extern.slf4j.Slf4j;
import me.cookie.old.nio.event.SocketChannelEventListenerGroup;
import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;
import me.cookie.old.nio.worker.SocketChannelWorkerGroup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
public class Server extends ConfigServer implements Group {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private Job acceptor;


    public Server(int port, SocketChannelEventListenerGroup eventListenerGroup, SocketChannelWorkerGroup workerGroup, Job job) throws IOException {
        setWorkJob(job);
        setWorkerGroup(workerGroup);
        setEventListenerGroup(eventListenerGroup);
        setPort(port);
        setJob(null);
        this.selector = SelectorProvider.provider().openSelector();
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(getPort()));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException, NioException {
        if(!isInit()){
            init();
        }
        log.debug("server start");
        while(true){
            selector.select(1000);
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            for(SelectionKey selectionKey : selectionKeySet){
                selectionKeySet.remove(selectionKey);
                try {
                    receive(selectionKey);
                }catch (NioException e){
                    log.error("acceptor exception", e);
                }
            }
        }
    }

    @Override
    public void init() {
        super.init();
        getEventListenerGroup().init();
        getWorkerGroup().init();
        log.debug("Server init");
    }

    @Override
    public void finish() {

    }

    @Override
    public void receive(Object object) throws NioException {
        SelectionKey selectionKey = (SelectionKey) object;
        if(selectionKey.isAcceptable()){
            acceptor.process(selectionKey.channel());
        }
    }

    @Override
    public void setJob(Job job) {
        this.acceptor = new ChannelAcceptor();
    }

    public class ChannelAcceptor implements Job{

        @Override
        public void init() {

        }

        @Override
        public void finish() {

        }

        @Override
        public Object process(Channel channel) throws NioException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) channel;
            SocketChannel socketChannel = null;
            try {
                socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                getEventListenerGroup().receive(socketChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
