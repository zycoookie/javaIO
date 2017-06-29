package me.cookie.old.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * Created by cookie on 2017/6/20.
 */
public class Client {

    private Selector selector;
    private SocketChannel socketChannel;

    public static void main(String[] args) throws IOException {
        new Client("127.0.0.1",8080);
    }

    public Client(String host, int port) throws IOException {
        selector = SelectorProvider.provider().openSelector();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_CONNECT);

        while(true) {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                selectionKeys.remove(selectionKey);
                if(selectionKey.isConnectable()){
                    if(socketChannel.finishConnect()){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.write(byteBuffer);
                        System.out.println("send success");
                    }
                }
                System.out.println(selectionKey.readyOps());
                if (selectionKey.isReadable()) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int size = socketChannel.read(byteBuffer);
                    if(size < 0){
                        selectionKey.cancel();
                        socketChannel.close();
                        return;
                    }
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    System.out.println(new String(bytes));
                }
            }
        }
    }
}
