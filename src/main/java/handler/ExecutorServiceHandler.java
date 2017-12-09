package handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

public class ExecutorServiceHandler<S> extends DecoratedHandler<S> {

    private ExecutorService pool;
    private Thread.UncaughtExceptionHandler exceptionHandler;

    public ExecutorServiceHandler(Handler<S> other,
                                  ExecutorService pool,
                                  Thread.UncaughtExceptionHandler exceptionHandler) {
        super(other);
        this.pool = pool;
        this.exceptionHandler = exceptionHandler;
    }

    public ExecutorServiceHandler(Handler<S> other, ExecutorService pool) {
        this(other, pool, (t, e) -> System.out.println("new exp " + e + t));
    }

    public void handle(S s) {
        pool.submit(
                new FutureTask<>(() -> {
                    super.handle(s);
                    return null;
                }) {
                    @Override
                    protected void setException(Throwable t) {
                        exceptionHandler.uncaughtException(Thread.currentThread(), t);
                    }
                });
    }
}
