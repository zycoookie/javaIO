package me.cookie.old.nio;

import me.cookie.old.nio.event.SocketChannelEventListenerGroup;
import me.cookie.old.nio.exception.NioException;
import me.cookie.old.nio.request.Job;
import me.cookie.old.nio.worker.SocketChannelWorkerGroup;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * Created by cookie on 2017/6/20.
 */
public class Bootstrap {

    public static void main(String[] args) throws IOException, NioException {
        Server server = new Server(8080,new SocketChannelEventListenerGroup(),new SocketChannelWorkerGroup(),new Job() {
            @Override
            public Object process(Channel channel) throws NioException {
                SocketChannel socketChannel = (SocketChannel) channel;
                return null;
            }

            @Override
            public void init() {

            }

            @Override
            public void finish() {

            }
        });
        server.start();
    }
}
