package svend.taikon.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockListener implements Listener {
    @EventHandler
    public void onPlayerPlaneBlock(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

}
