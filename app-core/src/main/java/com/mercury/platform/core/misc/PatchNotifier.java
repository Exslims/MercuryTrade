package com.mercury.platform.core.misc;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NewPatchMercuryEvent;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Константин on 09.12.2016.
 */
public class PatchNotifier {
    private final Logger logger = Logger.getLogger(PatchNotifier.class);
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    public PatchNotifier() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost",9999));
            socketChannel = serverSocketChannel.accept();
            logger.info("Connection to patch server established..." + socketChannel.getRemoteAddress());
            downloadPatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void downloadPatch(){
        try {
            String appPath =URLDecoder.decode(
                    PatchNotifier.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
            EventRouter.INSTANCE.fireEvent(new NewPatchMercuryEvent(appPath));
            socketChannel.close();
//            RandomAccessFile raf = new RandomAccessFile(appPath,"rw");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
