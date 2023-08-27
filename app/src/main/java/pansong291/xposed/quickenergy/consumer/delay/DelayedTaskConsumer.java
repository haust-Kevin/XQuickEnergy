package pansong291.xposed.quickenergy.consumer.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pansong291.xposed.quickenergy.AntForestToast;

public class DelayedTaskConsumer {

    private Thread executeThread;
    private ExecutorService threadPool;
    private boolean running = false;

    public void start(DelayQueue<DelayedTask> delayQueue, int threadPoolSize) {
        if (running) stop();
        threadPool = Executors.newFixedThreadPool(Math.min(1, threadPoolSize));
        executeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        DelayedTask delayedTask = delayQueue.take();
                        threadPool.execute(delayedTask.getTask());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        executeThread.start();
    }

    public void stop() {
        if (!running) return;
        executeThread.interrupt();
        running = false;
        executeThread = null;
        threadPool.shutdown();
    }
}
