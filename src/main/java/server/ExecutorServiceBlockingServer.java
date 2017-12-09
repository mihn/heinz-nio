package server;

import handler.ExecutorServiceHandler;
import handler.PrintingHandler;
import handler.TransmogrifyHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ExecutorServiceBlockingServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        ExecutorServiceHandler<Socket> handler = new ExecutorServiceHandler<>(
                new PrintingHandler<>(
                        new TransmogrifyHandler()),
                Executors.newCachedThreadPool(),
                (t, e) -> System.out.println("new exp " + e + t)
        );
        while (true) {
            Socket s = ss.accept();
            handler.handle(s);
        }
    }
}