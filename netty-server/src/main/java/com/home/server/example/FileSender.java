package com.home.server.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Константин on 09.12.2016.
 */
public class FileSender {
    public static void main(String[] args) {
        FileSender nioClient = new FileSender();
        SocketChannel socketChannel = nioClient.createChannel();
        nioClient.sendFile(socketChannel);
    }
    public SocketChannel createChannel(){
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            SocketAddress socketAddress = new InetSocketAddress("localhost",9999);
            socketChannel.connect(socketAddress);
            System.out.println("Connected, send file...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socketChannel;
    }
    public void sendFile(SocketChannel socketChannel){
        RandomAccessFile aFile = null;
        try {
//            File file = new File("D:\\PoeShortCast-SNAPSHOT-0.0.2.jar");
//            aFile = new RandomAccessFile(file,"r");
//            FileChannel inChannel = aFile.getChannel();
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            while (inChannel.read(buffer)>0){
//                buffer.flip();
//                socketChannel.write(buffer);
//                buffer.clear();
//            }
//            Thread.sleep(1000);
            System.out.println("End of file reached");
            socketChannel.close();
//            aFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
