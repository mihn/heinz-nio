package handler;

import util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

import static java.nio.channels.SelectionKey.OP_WRITE;

public class ReadHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public ReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {

        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocateDirect(80);
        int read = sc.read(buf);
        if (read == -1) {
            pendingData.remove(sc);
            System.out.println("Closing sc " + sc + " in read");
            sc.close();
            return;
        }
        if (read > 0) {
            Util.transmogrify(buf);
            pendingData.get(sc).add(buf);
            selectionKey.interestOps(OP_WRITE);
        }
    }
}
