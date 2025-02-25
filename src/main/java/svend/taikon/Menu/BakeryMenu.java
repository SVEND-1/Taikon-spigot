package svend.taikon.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import svend.taikon.Utility.MenuUtils;

public class BakeryMenu extends MenuManager{


    public BakeryMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return "Пекарня";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public void setMenuItems() {
        MenuUtils.ItemProfitableBuildings(inventory);
    }
}
