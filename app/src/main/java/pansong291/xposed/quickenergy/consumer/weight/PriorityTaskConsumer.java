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
    private boolean running = false;

    public void start(PriorityBlockingQueue<PriorityTask> priorityTasks) {
        if (running) stop();
        executeThread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        priorityTasks.take().getTask().run();
                        sleep(200);
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
    }
}
