package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.Buildings.GardenDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.*;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Product;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Task.ProductsTasks.GardenProductTask;
import svend.taikon.Utility.MenuUtils;
import svend.taikon.WorldEdit.WorldEditManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class GardenMenu extends BuildingMenu<Garden> {
    private Garden garden;
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

    public GardenMenu(Player player) {
        super(player, DataBaseManager.getInstance().getGardenDB());
        this.garden = buildingDB.read(player.getUniqueId());
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
                    Garden garden = buildingDB.read(player.getUniqueId());
                    if (user == null || garden == null) {
                        player.sendMessage("Ошибка данных");
                        return;
                    }

                    handlePlayerClick(player, clickedItemType, user, garden);
                } catch (Exception ex) {
                    player.sendMessage("Произошла ошибка");
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private void handlePlayerClick(Player player, Material clickedItemType, User user, Garden garden) {
        Product firstProduct = garden.getFirstProduct();
        Product secondProduct = garden.getSecondProduct();

        switch (clickedItemType) {
            case GREEN_STAINED_GLASS:
                handleUpIncome(user, garden);
                break;
            case OBSIDIAN:
                handleProductUpgrade(player, firstProduct, garden, user);
                break;
            case FEATHER:
                handleProductSell(player, firstProduct, user, garden);
                break;
            case PAPER:
                handleProductUpgrade(player, secondProduct, garden, user);
                break;
            case ARROW:
                handleProductSell(player, secondProduct, user, garden);
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
        GardenProductTask task = new GardenProductTask(playerId, (GardenDB) buildingDB, userDB);
        Garden garden = buildingDB.read(playerId);

        if (garden == null) return;

        if (productType == ProductType.FIRST) {
            task.startFirstProductTask(garden.getFirstProduct().getTime());
        } else {
            task.startSecondProductTask(garden.getSecondProduct().getTime());
        }
    }

}