package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class ToolsMenu extends MenuManager {

    private final UserDB userDB;
    private final ConnectToMongoDB database;

    public ToolsMenu(Player player) {
        super(player);
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
    }

    @Override
    public String getMenuName() {
        return "Магазин инструментов";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        ItemStack itemClick = e.getCurrentItem();

        if (itemClick == null || !itemClick.hasItemMeta() || !itemClick.getItemMeta().hasDisplayName()) {
            return;
        }
        if (player.getInventory().containsAtLeast(itemClick, 1)) {
            player.sendMessage("У вас уже есть этот предмет.");
            return;
        }
        int price = Integer.parseInt(itemClick.getItemMeta().getDisplayName());

        new BukkitRunnable() {
            @Override
            public void run() {
                User user = userDB.read(player.getUniqueId());

                if (user.getBalance() >= price) {
                    user.setBalance(user.getBalance() - price);
                    userDB.update(user);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.getInventory().addItem(itemClick);
                        }
                    }.runTask(Taikon.getPlugin());
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage("Недостаточно средств для покупки.");
                        }
                    }.runTask(Taikon.getPlugin());
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    @Override
    public void setMenuItems() {
        ItemStack woodenAxe = MenuUtils.createMenuItem(Material.WOODEN_AXE, "10");
        ItemStack stoneAxe = MenuUtils.createMenuItem(Material.STONE_AXE, "30");
        ItemStack ironAxe = MenuUtils.createMenuItem(Material.IRON_AXE, "70");

        ItemStack woodenPickaxe = MenuUtils.createMenuItem(Material.WOODEN_PICKAXE, "10");
        ItemStack stonePickaxe = MenuUtils.createMenuItem(Material.STONE_PICKAXE, "30");
        ItemStack ironPickaxe = MenuUtils.createMenuItem(Material.IRON_PICKAXE, "70");

        ItemStack woodenShovel = MenuUtils.createMenuItem(Material.WOODEN_SHOVEL, "10");
        ItemStack stoneShovel = MenuUtils.createMenuItem(Material.STONE_SHOVEL, "30");
        ItemStack ironShovel = MenuUtils.createMenuItem(Material.IRON_SHOVEL, "70");

        inventory.setItem(11, woodenAxe);
        inventory.setItem(13, woodenPickaxe);
        inventory.setItem(15, woodenShovel);

        inventory.setItem(20, stoneAxe);
        inventory.setItem(22, stonePickaxe);
        inventory.setItem(24, stoneShovel);

        inventory.setItem(29, ironAxe);
        inventory.setItem(31, ironPickaxe);
        inventory.setItem(33, ironShovel);
    }

}
