package handler;

import java.io.IOException;

public class PrintingHandler<S> extends DecoratedHandler<S> {

    public PrintingHandler(Handler<S> other) {
        super(other);
    }

    public void handle(S s) throws IOException {
        try {
            System.out.println("connection opened " + s);
            super.handle(s);
        } finally {
            System.out.println("disconnected from " + s);
        }
    }
}
