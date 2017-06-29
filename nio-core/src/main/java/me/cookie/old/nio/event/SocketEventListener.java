package me.cookie.old.nio.event;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.cookie.arch.LifeCycle;
import me.cookie.old.nio.exception.ExceptionCode;
import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;
import me.cookie.old.nio.worker.WorkerGroup;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
public class SocketEventListener implements LifeCycle,Job {

    private Selector selector;
    @Getter
    private boolean init = false;
    @Getter
    private boolean finish = false;
    @Setter
    private WorkerGroup workerGroup;
    private SocketChannelEventListenerGroup parent;
    public SocketEventListener(Selector selector,SocketChannelEventListenerGroup eventListenerGroup){
        this.parent = eventListenerGroup;
        this.selector = selector;
    }

    @Override
    public void init() {
        workerGroup = (WorkerGroup) parent.getServerContext().getWorkGroup();
        init = true;
    }

    @Override
    public void finish() {
        finish = true;
    }

    @Override
    public Object process(Channel channel) throws NioException {
        if(!isInit()){
            throw new NioException(ExceptionCode.NOT_INIT);
        }
        if(selector.isOpen()){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for(SelectionKey selectionKey : selectionKeys){
                    selectionKeys.remove(selectionKey);
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    workerGroup.receive(socketChannel);
                }
            } catch (IOException e) {
                log.error("selector exception",e);
            }
        }else{
            throw new NioException(ExceptionCode.SELECTOR_NOT_OPEN);
        }
        return null;
    }
}
