package pansong291.xposed.quickenergy.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import pansong291.xposed.quickenergy.util.Config;

public class DelayedTaskConsumer {

    private DelayQueue<DelayedTask> delayQueue;
    private Thread executeThread;
    private ExecutorService threadPool;
    private boolean running = false;

    private int threadPoolSize = 1;

    public void start(DelayQueue<DelayedTask> delayQueue) {
        if (running) stop();
        this.delayQueue = delayQueue;
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
        executeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    DelayedTask delayedTask = delayQueue.poll();
                    if (delayedTask == null) continue;
                    threadPool.execute(delayedTask.getTask());
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
        delayQueue = null;
        threadPool.shutdown();
    }
}
