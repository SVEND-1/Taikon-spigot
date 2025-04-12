package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.Buildings.GardenDB;
import svend.taikon.DataBase.ModelDAO.Buildings.RestaurantDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.*;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Buildings.Restaurant;
import svend.taikon.Model.Product;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Task.ProductsTasks.GardenProductTask;
import svend.taikon.Task.ProductsTasks.RestaurantProductTask;
import svend.taikon.Utility.MenuUtils;
import svend.taikon.WorldEdit.WorldEditManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RestaurantMenu extends BuildingMenu<Restaurant> {
    private Restaurant restaurant;
    private static final Map<ProductType, ProductUpgrade> UPGRADE_CONFIG = new HashMap<>();

    static {
        // Конфигурация улучшений для первого продукта (Роза)
        UPGRADE_CONFIG.put(ProductType.FIRST, new ProductUpgrade(
                new ResourceCost(30, 5, 0),    // Ресурсы для 1 уровня
                new LargeNumber("7500"),       // Цена для 1 уровня
                new ResourceCost(150, 25, 0),  // Ресурсы для 2 уровня
                new LargeNumber("37500")       // Цена для 2 уровня
        ));

        // Конфигурация улучшений для второго продукта (Букет)
        UPGRADE_CONFIG.put(ProductType.SECOND, new ProductUpgrade(
                new ResourceCost(300, 50, 10),
                new LargeNumber("750000"),
                new ResourceCost(1500, 250, 50),
                new LargeNumber("75000000")
        ));
    }

    public RestaurantMenu(Player player) {
        super(player, DataBaseManager.getInstance().getRestaurantDB());
        this.restaurant = buildingDB.read(player.getUniqueId());
    }

    @Override
    public String getMenuName() {
        return "Сад";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        MenuUtils.ItemProfitableBuildings(inventory);
        // Здесь можно добавить специфичные для сада предметы в меню
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!e.getView().getTitle().equals(getMenuName())) return;

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : Material.AIR;

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    User user = userDB.read(player.getUniqueId());
                    Restaurant restaurant = buildingDB.read(player.getUniqueId());
                    if (user == null || restaurant == null) {
                        player.sendMessage("Ошибка данных");
                        return;
                    }

                    handlePlayerClick(player, clickedItemType, user, restaurant);
                } catch (Exception ex) {
                    player.sendMessage("Произошла ошибка");
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private void handlePlayerClick(Player player, Material clickedItemType, User user, Restaurant restaurant) {
        Product firstProduct = restaurant.getFirstProduct();
        Product secondProduct = restaurant.getSecondProduct();

        switch (clickedItemType) {
            case GREEN_STAINED_GLASS:
                handleUpIncome(user, restaurant);
                break;
            case OBSIDIAN:
                handleProductUpgrade(player, firstProduct, restaurant, user);
                break;
            case FEATHER:
                handleProductSell(player, firstProduct, user, restaurant);
                break;
            case PAPER:
                handleProductUpgrade(player, secondProduct, restaurant, user);
                break;
            case ARROW:
                handleProductSell(player, secondProduct, user, restaurant);
                break;
            case RED_STAINED_GLASS:
                player.closeInventory();
                break;
        }
    }

    @Override
    protected Map<ProductType, ProductUpgrade> getUpgradeConfig() {
        return UPGRADE_CONFIG;
    }

    @Override
    protected void buildBuilding(Player player) {
        final File file = new File(Taikon.getPlugin().getDataFolder(), "schematic/garden.schem");
        Location location = new Location(player.getWorld(), 109, 99, 136);
        WorldEditManager.paste(location, file);
    }

    @Override
    protected void startProductionTask(UUID playerId, ProductType productType) {
        RestaurantProductTask task = new RestaurantProductTask(playerId, (RestaurantDB) buildingDB, userDB);
        Restaurant restaurant = buildingDB.read(playerId);

        if (restaurant == null) return;

        if (productType == ProductType.FIRST) {
            task.startFirstProductTask(restaurant.getFirstProduct().getTime());
        } else {
            task.startSecondProductTask(restaurant.getSecondProduct().getTime());
        }
    }

}