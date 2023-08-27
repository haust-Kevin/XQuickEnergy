package pansong291.xposed.quickenergy.consumer.weight;

public class PriorityTask implements Comparable<PriorityTask>{
    private Runnable task;
    private int weight;

    @Override
    public int compareTo(PriorityTask priorityTask) {
        return Integer.compare(weight, priorityTask.weight);
    }

    public PriorityTask(Runnable task, int weight) {
        this.task = task;
        this.weight = weight;
    }

    public Runnable getTask() {
        return task;
    }

    public int getWeight() {
        return weight;
    }
}
