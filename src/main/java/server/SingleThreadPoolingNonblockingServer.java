package server;

import handler.TransmogrifyChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;

public class SingleThreadPoolingNonblockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        TransmogrifyChannelHandler handler = new TransmogrifyChannelHandler();
        Collection<SocketChannel> sockets = new ArrayList<>();
        while (true) {
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                sockets.add(sc);
                System.out.println("connected to channel " + sc);
                sc.configureBlocking(false);
            }
            for (SocketChannel socket : sockets) {
                if (socket.isConnected()) {
                    handler.handle(socket);
                }
            }
            sockets.removeIf(socketChannel -> !socketChannel.isConnected());
        }
    }
}