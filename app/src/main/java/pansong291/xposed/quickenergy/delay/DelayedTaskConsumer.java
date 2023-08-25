package pansong291.xposed.quickenergy.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                    DelayedTask delayedTask = null;
                    delayedTask = delayQueue.element();
                    synchronized (delayQueue){
                        delayedTask = delayQueue.poll();
                    }
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
        threadPool.shutdown();
    }
}
