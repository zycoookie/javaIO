package me.cookie.old.nio;

import lombok.extern.slf4j.Slf4j;
import me.cookie.old.nio.exception.ExceptionCode;
import me.cookie.old.nio.exception.NioException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by cookie on 2017/6/20.
 */
@Slf4j
public class ChannelReader {

    public static byte[] getBytes(SocketChannel channel) throws NioException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while(true){
            try {
                int size = channel.read(byteBuffer);
                if(size < 0){
                    log.info("channel close");
                    throw new NioException(ExceptionCode.CHANNEL_CLOSE);
                }

                if(size == byteBuffer.capacity()){

                }

            } catch (IOException e) {
                log.error("channel io exception");
                throw new NioException(ExceptionCode.CHANNEL_CLOSE,e);
            }
        }
    }
}
