package svend.taikon.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import svend.taikon.Task.AddIncomeTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLeaveListener implements Listener {
    private final HashMap<UUID, AddIncomeTask> activeTasks;

    public PlayerLeaveListener(HashMap<UUID, AddIncomeTask> activeTasks) {
        this.activeTasks = activeTasks;
    }

    @EventHandler
    public void onQuitPlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (activeTasks.containsKey(playerId)) {
            AddIncomeTask task = activeTasks.get(playerId);
            task.cancel();
            activeTasks.remove(playerId);
        }
    }
}
