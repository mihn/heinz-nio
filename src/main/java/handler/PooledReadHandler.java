package handler;

import util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static java.nio.channels.SelectionKey.OP_WRITE;

public class PooledReadHandler implements Handler<SelectionKey> {
    private final ExecutorService pool;
    private final Queue<Runnable> selectorActions;
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

    public PooledReadHandler(ExecutorService pool,
                             Queue<Runnable> selectorActions,
                             Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
        this.pool = pool;
        this.selectorActions = selectorActions;
        this.pendingData = pendingData;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocateDirect(80);
        int read = sc.read(buf);
        if (read == -1) {
            pendingData.remove(sc);
            System.out.println("Closing sc " + sc + " in pooledRead");
            sc.close();
            return;
        }
        if (read > 0) {
            pool.submit(() -> {
                Util.transmogrify(buf);
                pendingData.get(sc).add(buf);
                selectorActions.add(() -> selectionKey.interestOps(OP_WRITE));
                selectionKey.selector().wakeup();
            });

        }
    }
}
