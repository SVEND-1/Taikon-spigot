package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.Taikon;

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

        if (e.getView().getTitle().equals(getMenuName())) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !RESOURCE_PRICES.containsKey(e.getCurrentItem().getType())) {
                return;
            }
            Player player = (Player) e.getWhoClicked();
            Material clickedItemType = e.getCurrentItem().getType();

            new BukkitRunnable() {
                @Override
                public void run() {
                    Resource resource = resourceDB.read(player.getUniqueId());
                    User user = userDB.read(player.getUniqueId());

                    int amount = RESOURCE_PRICES.get(clickedItemType).apply(resource);

                    sellResource(resource, user, amount, RESET_RESOURCES.get(clickedItemType));
                }
            }.runTaskAsynchronously(Taikon.getPlugin());
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack whiteTulip = createMenuItem(Material.WHITE_TULIP, "1");
        ItemStack oakLog = createMenuItem(Material.OAK_LOG, "5");
        ItemStack stone = createMenuItem(Material.STONE, "10");
        ItemStack sand = createMenuItem(Material.SAND, "15");

        inventory.setItem(10, whiteTulip);
        inventory.setItem(12, oakLog);
        inventory.setItem(14, stone);
        inventory.setItem(16, sand);
    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private void sellResource(Resource resource, User user, int amount, Consumer<Resource> resetResource) {
        user.setBalance(user.getBalance() + amount);
        resetResource.accept(resource);
        userDB.update(user);
        resourceDB.update(resource);
    }
}