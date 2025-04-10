package svend.taikon.Task.ProductsTasks;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProductTaskManager {
    private static final Map<UUID, BakeryProductTask> activeTasks = new ConcurrentHashMap<>();

    public static boolean hasTask(UUID playerId) {
        return activeTasks.containsKey(playerId);
    }

    public static void addTask(UUID playerId, BakeryProductTask task) {
        activeTasks.put(playerId, task);
    }

    public static void stopTask(UUID playerId) {
        BakeryProductTask task = activeTasks.get(playerId);
        if (task != null) {
            task.cancel();
            activeTasks.remove(playerId);
        }
    }
}