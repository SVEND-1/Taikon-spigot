package svend.taikon.Admin.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import svend.taikon.Admin.menu.AdminBanMenu;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Utility.MenuUtils;

public class AdminMenu extends MenuManager {
    public AdminMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return "Admin menu";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        if (!e.getView().getTitle().equals(getMenuName())) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : null;

        if(!player.hasPermission("permissions.Permissions") || !player.isOp()){
            player.sendMessage("Это только для админов'");
            player.closeInventory();
            return;
        }

        if (clickedItemType == null) {
            return;
        }

        switch (clickedItemType) {
            case PLAYER_HEAD:
                AdminBanMenu adminBanMenu = new AdminBanMenu(player);
                adminBanMenu.open();
                break;
            case ENDER_PEARL:
                AdminTeleportMenu adminTeleportMenu = new AdminTeleportMenu(player);
                adminTeleportMenu.open();
                break;
            case PAPER:
                AdminStatisticMenu adminStatisticMenu = new AdminStatisticMenu(player);
                adminStatisticMenu.open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack banHead = MenuUtils.createMenuItem(Material.PLAYER_HEAD,"Бан игрока");
        ItemStack teleportToPlayer = MenuUtils.createMenuItem(Material.ENDER_PEARL,"Телепортироваться к игроку");
        ItemStack statisticPlayer = MenuUtils.createMenuItem(Material.PAPER,"Статистика игроков");

        inventory.setItem(3,banHead);
        inventory.setItem(4,teleportToPlayer);
        inventory.setItem(5,statisticPlayer);
    }
}
