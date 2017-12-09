package server;

import handler.BlockingChannelHandler;
import handler.ExecutorServiceHandler;
import handler.PrintingHandler;
import handler.TransmogrifyChannelHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

public class BlockingNIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ExecutorServiceHandler<SocketChannel> handler = new ExecutorServiceHandler<>(
                new PrintingHandler<>(
                        new BlockingChannelHandler(
                                new TransmogrifyChannelHandler())),
                Executors.newCachedThreadPool()
        );
        while (true) {
            SocketChannel sc = ssc.accept();
            handler.handle(sc);
        }
    }
}