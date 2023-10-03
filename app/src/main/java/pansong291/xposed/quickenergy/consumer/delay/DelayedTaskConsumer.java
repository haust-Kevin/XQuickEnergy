package pansong291.xposed.quickenergy.consumer.delay;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pansong291.xposed.quickenergy.AntForestToast;

public class DelayedTaskConsumer {

    private Thread executeThread;
    private ExecutorService threadPool;
    private boolean running = false;

    private Runnable mainRunnable;
    private int threadPoolSize;

    public void start(DelayQueue<DelayedTask> delayQueue, int threadPoolSize) {
        if (running) stop();
        this.threadPoolSize = threadPoolSize;
        running = true;
        threadPool = Executors.newFixedThreadPool(Math.min(1, threadPoolSize));
        mainRunnable = () -> {
            while (!Thread.interrupted()) {
                try {
                    DelayedTask delayedTask = delayQueue.take();
                    threadPool.execute(delayedTask.getTask());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executeThread = new Thread(mainRunnable);
        executeThread.start();
    }

    public void stop() {
        if (!running) return;
        executeThread.interrupt();
        running = false;
        executeThread = null;
        threadPool.shutdown();
        threadPool = null;
        mainRunnable = null;
    }

//    public void restartThread() {
//        assert (running);
//        executeThread.interrupt();
//        threadPool.shutdown();
//        threadPool = Executors.newFixedThreadPool(Math.min(1, threadPoolSize));
//        executeThread = new Thread(mainRunnable);
//        executeThread.start();
//    }

    public boolean isRunning() {
        return running;
    }
}
