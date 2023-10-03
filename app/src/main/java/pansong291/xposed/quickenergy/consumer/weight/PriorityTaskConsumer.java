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

    private Runnable mainRunnable = null;
    public void start(PriorityBlockingQueue<PriorityTask> priorityTasks) {
        if (running) stop();
        running = true;
        mainRunnable = () -> {
            while (!Thread.interrupted()) {
                try {
                    priorityTasks.take().getTask().run();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        executeThread = new Thread(mainRunnable);
        executeThread.start();
    }

//    public void restartThread() {
//        assert (running && executeThread!=null);
//        executeThread.interrupt();
//        executeThread = new Thread(mainRunnable);
//        executeThread.start();
//    }

    public void stop() {
        if (!running) return;
        executeThread.interrupt();
        running = false;
        executeThread = null;
    }

    public boolean isRunning() {
        return running;
    }
}
