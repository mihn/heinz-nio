package handler;

import java.io.IOException;
import java.io.UncheckedIOException;

public class UncheckedIOExceptionConvertHandler<S> extends DecoratedHandler<S> {

    public UncheckedIOExceptionConvertHandler(Handler<S> other) {
        super(other);
    }

    @Override
    public void handle(S s) {
        try {
            super.handle(s);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
