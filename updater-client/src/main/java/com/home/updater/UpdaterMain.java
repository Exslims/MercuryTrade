package com.home.updater;

import java.nio.channels.SocketChannel;

/**
 * Created by Константин on 09.12.2016.
 */
public class UpdaterMain {
    public static void main(String[] args) {
        AppPatchReceiver patchReceiver = new AppPatchReceiver();
        SocketChannel socketChannel = patchReceiver.createServerSocketChannel();
        patchReceiver.readFileFromSocket(socketChannel);
    }
}
