package svend.taikon.Donat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class DonatMenu extends MenuManager {
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
                BusterIncome busterIncome = new BusterIncome(userDB);

                switch (clickedItemType) {
                    case FEATHER:
                        Fly(player, user);
                        break;
                    case DIAMOND:
                        busterIncome.turnOnTheLocalBooster(player);
                        break;
                    case DIAMOND_BLOCK:
                        busterIncome.turnOnTheGlobalBooster();
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
        ItemStack fly = MenuUtils.createMenuItemWithLore(Material.FEATHER,"Полет","Цена: 100","Вы сможете летать по карте");
        ItemStack localBoost = MenuUtils.createMenuItemWithLore(Material.DIAMOND,"Локальный буст х2","Цена: 120","Время: 30мин");
        ItemStack globalBoost = MenuUtils.createMenuItemWithLore(Material.DIAMOND_BLOCK,"Глобальный буст х2","Цена: 120","Время: 30мин");
        ItemStack diamondAxe = MenuUtils.createMenuItemWithLore(Material.DIAMOND_AXE,"Алмазный топор","Цена: 150","Доход +4");
        ItemStack diamondPickaxe = MenuUtils.createMenuItemWithLore(Material.DIAMOND_PICKAXE,"Алмазная кирка","Цена: 150","Доход +4");
        ItemStack diamondShovel = MenuUtils.createMenuItemWithLore(Material.DIAMOND_SHOVEL,"Алмазная лопата","Цена: 150","Доход +4");

        inventory.setItem(10,fly);
        inventory.setItem(12,localBoost);
        inventory.setItem(13,globalBoost);
        inventory.setItem(14,diamondAxe);
        inventory.setItem(15,diamondPickaxe);
        inventory.setItem(16,diamondShovel);
    }


}
