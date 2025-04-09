package svend.taikon.Task;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProductTaskManager {
    private static final Map<UUID, ProductTask> activeTasks = new ConcurrentHashMap<>();

    public static boolean hasTask(UUID playerId) {
        return activeTasks.containsKey(playerId);
    }

    public static void addTask(UUID playerId, ProductTask task) {
        activeTasks.put(playerId, task);
    }

    public static void stopTask(UUID playerId) {
        ProductTask task = activeTasks.get(playerId);
        if (task != null) {
            task.cancel();
            activeTasks.remove(playerId);
        }
    }
}