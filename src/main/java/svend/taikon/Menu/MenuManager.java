package svend.taikon.Menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class MenuManager implements InventoryHolder {
    protected Inventory inventory;
    protected Player player;

    public MenuManager(Player player){
        this.player = player;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setMenuItems();
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open(){

        inventory = Bukkit.createInventory(this,getSlots(),getMenuName());

        this.setMenuItems();//тут типо положили вещи

        player.openInventory(inventory);

    }
}
