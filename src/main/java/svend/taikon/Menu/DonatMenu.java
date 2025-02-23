package svend.taikon.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DonatMenu extends MenuManager{
    public DonatMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return "Донат";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e){

    }

    @Override
    public void setMenuItems() {

    }
}
