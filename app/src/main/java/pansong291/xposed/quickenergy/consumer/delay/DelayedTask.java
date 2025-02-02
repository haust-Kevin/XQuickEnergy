package pansong291.xposed.quickenergy.consumer.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedTask implements Delayed {

    private long runTime;
    private Runnable task;

    public void setTask(Runnable task) {
        this.task = task;
    }

    public void setDelay(long delayTime, TimeUnit unit) {
        this.runTime = unit.convert(delayTime, TimeUnit.MILLISECONDS) + System.currentTimeMillis();
    }

    public DelayedTask() {

    }

    public DelayedTask(Runnable task, long delayTime, TimeUnit unit) {
        this.task = task;
        this.runTime = unit.convert(delayTime, TimeUnit.MILLISECONDS) + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long offset = runTime - System.currentTimeMillis();
        return unit.convert(offset, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.runTime - ((DelayedTask) delayed).getRunTime());
    }

    public long getRunTime() {
        return runTime;
    }

    public Runnable getTask() {
        return task;
    }
}
