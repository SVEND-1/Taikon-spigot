package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
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
    private final ConnectToMongoDB database;
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
        this.database = new ConnectToMongoDB();
        this.resourceDB = new ResourceDB(database.getDatabase());
        this.userDB = new UserDB(database.getDatabase());
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
                sell(new ItemStack(Material.WHITE_TULIP),1);
                break;
            case OAK_LOG:
                sell(new ItemStack(Material.OAK_LOG),5);
                break;
            case STONE:
                sell(new ItemStack(Material.STONE),10);
                break;
            case SAND:
                sell(new ItemStack(Material.SAND),15);
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


    private void sellResource(Resource resource, User user, int price, Consumer<Resource> resetResource) {
        user.setBalance(user.getBalance().add(new LargeNumber(String.valueOf(price))));
        resetResource.accept(resource);
        userDB.update(user);
        resourceDB.update(resource);
    }
    private void sell(ItemStack itemSell, int price) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Material material = itemSell.getType();
                Consumer<Resource> resetConsumer = RESET_RESOURCES.get(material);

                if (resetConsumer == null) {
                    return;
                }

                Resource resource = resourceDB.read(player.getUniqueId());
                User user = userDB.read(player.getUniqueId());

                sellResource(resource, user, price, resetConsumer);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }
}