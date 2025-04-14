package svend.taikon.Donat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class DonatMenu extends MenuManager {
    private final UserDB userDB;
    private final DataBaseManager dataBaseManager;
    public DonatMenu(Player player) {
        super(player);
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
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
                    case OAK_PLANKS:
                        BusterResource.turnOnTheLocalBoosterWood(player);
                        break;
                    case STONE_SLAB:
                        BusterResource.turnOnTheLocalBoosterStone(player);
                        break;
                    case RED_SAND:
                        BusterResource.turnOnTheLocalBoosterSand(player);
                        break;
                    case OAK_LOG:
                        BusterResource.turnOnTheGlobalBoosterWood();
                        break;
                    case STONE:
                        BusterResource.turnOnTheGlobalBoosterStone();
                        break;
                    case SAND:
                        BusterResource.turnOnTheGlobalBoosterSand();
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

        ItemStack localBoostWood = MenuUtils.createMenuItemWithLore(Material.OAK_PLANKS,"Локальный буст дерево х2","Цена: 120","Время: 30мин");
        ItemStack localBoostStone = MenuUtils.createMenuItemWithLore(Material.STONE_SLAB,"Локальный буст камня х2","Цена: 120","Время: 30мин");
        ItemStack localBoostSand = MenuUtils.createMenuItemWithLore(Material.RED_SAND,"Локальный буст песка х2","Цена: 120","Время: 30мин");

        ItemStack globalBoostWood = MenuUtils.createMenuItemWithLore(Material.OAK_LOG,"Глобальный буст дерево х2","Цена: 120","Время: 30мин");
        ItemStack globalBoostStone = MenuUtils.createMenuItemWithLore(Material.STONE,"Глобальный буст камня х2","Цена: 120","Время: 30мин");
        ItemStack globalBoostSand = MenuUtils.createMenuItemWithLore(Material.SAND,"Глобальный буст песка х2","Цена: 120","Время: 30мин");

        ItemStack diamondAxe = MenuUtils.createMenuItemWithLore(Material.DIAMOND_AXE,"Алмазный топор","Цена: 150","Доход +4");
        ItemStack diamondPickaxe = MenuUtils.createMenuItemWithLore(Material.DIAMOND_PICKAXE,"Алмазная кирка","Цена: 150","Доход +4");
        ItemStack diamondShovel = MenuUtils.createMenuItemWithLore(Material.DIAMOND_SHOVEL,"Алмазная лопата","Цена: 150","Доход +4");

        inventory.setItem(9,fly);
        inventory.setItem(11,localBoost);
        inventory.setItem(12,globalBoost);

        inventory.setItem(5,globalBoostWood);
        inventory.setItem(14,globalBoostStone);
        inventory.setItem(23,globalBoostSand);

        inventory.setItem(6,localBoostWood);
        inventory.setItem(15,localBoostStone);
        inventory.setItem(24,localBoostSand);

        inventory.setItem(8,diamondAxe);
        inventory.setItem(17,diamondPickaxe);
        inventory.setItem(26,diamondShovel);
    }


}
