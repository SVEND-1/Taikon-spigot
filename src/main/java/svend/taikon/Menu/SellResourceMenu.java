package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Donat.BusterResource;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class SellResourceMenu extends MenuManager {
    private final DataBaseManager dataBaseManager;
    private final ResourceDB resourceDB;
    private final UserDB userDB;
    private static final Map<Material, Function<Resource, Integer>> RESOURCE_PRICES = new HashMap<>();
    private static final Map<Material, Consumer<Resource>> RESET_RESOURCES = new HashMap<>();

    static {
        RESOURCE_PRICES.put(Material.WHITE_TULIP, Resource::getFlowers);
        RESOURCE_PRICES.put(Material.OAK_LOG, r -> r.getWood() * 5);
        RESOURCE_PRICES.put(Material.STONE, r -> r.getStone() * 10);
        RESOURCE_PRICES.put(Material.SAND, r -> r.getSand() * 15);

        RESET_RESOURCES.put(Material.WHITE_TULIP, r -> r.setFlowers(0));
        RESET_RESOURCES.put(Material.OAK_LOG, r -> r.setWood(0));
        RESET_RESOURCES.put(Material.STONE, r -> r.setStone(0));
        RESET_RESOURCES.put(Material.SAND, r -> r.setSand(0));
    }

    public SellResourceMenu(Player player) {
        super(player);
        this.dataBaseManager = DataBaseManager.getInstance();
        this.resourceDB = dataBaseManager.getResourceDB();
        this.userDB = dataBaseManager.getUserDB();
    }

    @Override
    public String getMenuName() {
        return "Продажа ресурсов";
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
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        switch (clickedItem.getType()) {
            case WHITE_TULIP:
                sell(new ItemStack(Material.WHITE_TULIP));
                break;
            case OAK_LOG:
                sell(new ItemStack(Material.OAK_LOG));
                break;
            case STONE:
                sell(new ItemStack(Material.STONE));
                break;
            case SAND:
                sell(new ItemStack(Material.SAND));
                break;
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack whiteTulip = MenuUtils.createMenuItemWithLore(
                Material.WHITE_TULIP,
                "§d✦ §fБелый тюльпан §d✦",
                "§8▸ §7Цена: §a1 монета",
                null
        );

        ItemStack oakLog = MenuUtils.createMenuItemWithLore(
                Material.OAK_LOG,
                "§6✦ §eДубовое бревно §6✦",
                "§8▸ §7Цена: §a5 монет",
                null
        );

        ItemStack stone = MenuUtils.createMenuItemWithLore(
                Material.STONE,
                "§7✦ §fКамень §7✦",
                "§8▸ §7Цена: §a10 монет",
                null
        );

        ItemStack sand = MenuUtils.createMenuItemWithLore(
                Material.SAND,
                "§e✦ §fПесок §e✦",
                "§8▸ §7Цена: §a15 монет",
                null
        );

        inventory.setItem(10, whiteTulip);
        inventory.setItem(12, oakLog);
        inventory.setItem(14, stone);
        inventory.setItem(16, sand);
    }


    private void sell(ItemStack itemSell) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Material material = itemSell.getType();

                    Function<Resource, Integer> getAmountFunc = RESOURCE_PRICES.get(material);
                    Consumer<Resource> resetFunc = RESET_RESOURCES.get(material);

                    if (getAmountFunc == null || resetFunc == null) return;

                    Resource resource = resourceDB.read(player.getUniqueId());
                    User user = userDB.read(player.getUniqueId());

                    if (resource == null || user == null) {
                        player.sendMessage("Ошибка данных");
                        return;
                    }

                    int amount = getAmountFunc.apply(resource);

                    if (amount <= 0) {
                        player.sendMessage("§cНет ресурсов для продажи");
                        return;
                    }

                    if(material == Material.OAK_LOG) {
                        amount *= BusterResource.isPlayerInSetWood(player) ? 2 : 1;
                        amount *= BusterResource.globalBoostActiveWood ? 2 : 1;
                    } else if (material == Material.STONE) {
                        amount *= BusterResource.isPlayerInSetStone(player) ? 2 : 1;
                        amount *= BusterResource.globalBoostActiveStone ? 2 : 1;
                    } else if (material == Material.SAND){
                        amount *= BusterResource.isPlayerInSetSand(player) ? 2 : 1;
                        amount *= BusterResource.globalBoostActiveSand ? 2 : 1;
                    }

                    LargeNumber totalPrice = new LargeNumber(String.valueOf(amount));

                    user.setBalance(user.getBalance().add(totalPrice));
                    resetFunc.accept(resource);

                    boolean success = userDB.update(user) && resourceDB.update(resource);

                    if (success) {
                        player.sendMessage("§aПродано! +" + totalPrice +" монет");
                    } else {
                        player.sendMessage("§cОшибка при сохранении");
                    }

                } catch (Exception e) {
                    player.sendMessage("§cОшибка при продаже");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }
}