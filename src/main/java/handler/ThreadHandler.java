package handler;

public class ThreadHandler<S> extends UncheckedIOExceptionConvertHandler<S> {

    public ThreadHandler(Handler<S> other) {
        super(other);
    }

    public void handle(S s) {
        new Thread(() -> super.handle(s)).start();
    }
}
