package com.home.updater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Константин on 09.12.2016.
 */
public class AppPatchReceiver {
    public SocketChannel createServerSocketChannel(){
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));
            socketChannel = serverSocketChannel.accept();
            System.out.println("Connection established...." + socketChannel.getRemoteAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }
    public void readFileFromSocket(SocketChannel socketChannel){
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("D:\\PoeShortCast-SNAPSHOT-0.0.2-test.jar","rw");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel fileChannel = aFile.getChannel();
            while (socketChannel.read(buffer)>0){
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            Thread.sleep(1000);
            fileChannel.close();
            System.out.println("End of file reached.. Closing channel");
            socketChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
