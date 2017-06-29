package me.cookie.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by cookie on 2017/6/23.
 */
public class TestNioChannel {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("/Users/legend/1.txt","rw");
        FileChannel fileChannel = file.getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,1024);
        byte[] bytes = new String("1234567890").getBytes("UTF-8");
        mappedByteBuffer.put(bytes);
        fileChannel.position(20);
        mappedByteBuffer.flip();
        fileChannel.write(mappedByteBuffer);
        fileChannel.force(true);
        bytes = new String("zxcvbnasfwqerwer").getBytes("UTF-8");
        mappedByteBuffer.clear();
        mappedByteBuffer.put(bytes);
        mappedByteBuffer.flip();
        fileChannel.write(mappedByteBuffer);
        fileChannel.force(true);
        fileChannel.close();
        file.close();
    }
}
