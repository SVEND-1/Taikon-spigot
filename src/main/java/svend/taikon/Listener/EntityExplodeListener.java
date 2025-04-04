package svend.taikon.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener {
    @EventHandler
    public void EntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

}
