package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class DonatMenu extends MenuManager{
    private final UserDB userDB;
    private final ConnectToMongoDB database;
    public DonatMenu(Player player) {
        super(player);
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
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
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        if (!e.getView().getTitle().equals(getMenuName())) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : null;

        if (clickedItemType == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                User user = userDB.read(player.getUniqueId());

                switch (clickedItemType) {
                    case FEATHER:
                        Fly(player, user);
                        break;
                    case DIAMOND_AXE:
                    case DIAMOND_PICKAXE:
                    case DIAMOND_SHOVEL:
                        Tools(player, user, clickedItemType);
                        break;
                }

                userDB.update(user);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }


    private void Fly(Player player, User user) {
        if (user.getBalance().leftOrEqual(new LargeNumber("100"))) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                player.closeInventory();
                player.sendMessage("Полет активирован!");

                user.setBalance(user.getBalance().subtract(new LargeNumber("100")));
            } else {
                player.sendMessage("У вас уже есть полет");
            }
        } else {
            player.sendMessage("Недостаточно средств");
        }
    }

    private void Tools(Player player, User user, Material clickedItemType) {
        if (user.getBalance().leftOrEqual(new LargeNumber("150"))) {
            ItemStack itemToCheck = new ItemStack(clickedItemType);
            ItemStack itemToAdd = new ItemStack(clickedItemType);

            if (!player.getInventory().containsAtLeast(itemToCheck, 1)) {
                player.getInventory().addItem(itemToAdd);
                player.sendMessage("Вы успешно купили предмет!");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        user.setBalance(user.getBalance().subtract(new LargeNumber("150")));
                        userDB.update(user);
                    }
                }.runTaskAsynchronously(Taikon.getPlugin());
            } else {
                player.sendMessage("У вас уже куплен этот предмет");
            }
        } else {
            player.sendMessage("Недостаточно средств");
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack fly = MenuUtils.createMenuItem(Material.FEATHER,"Полет: 100");
        ItemStack diamondAxe = MenuUtils.createMenuItem(Material.DIAMOND_AXE,"Алмазный топор: 150");
        ItemStack diamondPickaxe = MenuUtils.createMenuItem(Material.DIAMOND_PICKAXE,"Алмазная кирка: 150");
        ItemStack diamondShovel = MenuUtils.createMenuItem(Material.DIAMOND_SHOVEL,"Алмазная лопата: 150");

        inventory.setItem(10,fly);
        inventory.setItem(14,diamondAxe);
        inventory.setItem(15,diamondPickaxe);
        inventory.setItem(16,diamondShovel);
    }


}
