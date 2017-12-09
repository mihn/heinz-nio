package handler;

import java.io.IOException;

public abstract class DecoratedHandler<S> implements Handler<S> {
    protected final Handler<S> other;

    protected DecoratedHandler(Handler<S> other) {
        this.other = other;
    }

    @Override
    public void handle(S s) throws IOException {
        other.handle(s);
    }
}
