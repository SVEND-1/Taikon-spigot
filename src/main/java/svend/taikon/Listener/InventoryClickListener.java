package svend.taikon.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import svend.taikon.Menu.MenuManager;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof MenuManager) {
            MenuManager menu = (MenuManager) e.getInventory().getHolder();
            menu.handleMenu(e);
        } else {
            System.out.println("Inventory holder is not MenuManager");
        }
    }
}
