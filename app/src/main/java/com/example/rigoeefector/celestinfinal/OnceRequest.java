package com.example.rigoeefector.celestinfinal;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by rigoeefector on 8/30/20.
 */

public class OnceRequest {
    private final AtomicBoolean done = new AtomicBoolean();

    public void run(Runnable task) {
        if (done.get()) return;
        if (done.compareAndSet(false, true)) {
            task.run();
        }
    }
}
