package pansong291.xposed.quickenergy.consumer.weight;

import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import pansong291.xposed.quickenergy.AntForestToast;
import pansong291.xposed.quickenergy.consumer.delay.DelayedTask;

public class PriorityTaskConsumer {
    private Thread executeThread;
    private ExecutorService threadPool;
    private boolean running = false;

    public void start(PriorityBlockingQueue<PriorityTask> priorityTasks, int threadPoolSize) {
        if (running) stop();
        threadPool = Executors.newFixedThreadPool(Math.min(1, threadPoolSize));
        executeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        PriorityTask priorityTask = priorityTasks.take();
                        threadPool.execute(() -> {
                            // 让线程池中的线程 sleep 而不让 `executeThread` sleep
                            priorityTask.getTask().run();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        });

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
