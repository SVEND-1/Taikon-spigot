package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.Buildings.BakeryDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.BuildingMenu;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductType;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductUpgrade;
import svend.taikon.Menu.BuildingsMenu.Settings.ResourceCost;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Product;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Task.ProductsTasks.BakeryProductTask;
import svend.taikon.Utility.MenuUtils;
import svend.taikon.WorldEdit.WorldEditManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BakeryMenu extends BuildingMenu<Bakery> {
    private Bakery bakery;
    private static final Map<ProductType, ProductUpgrade> UPGRADE_CONFIG = new HashMap<>();

    static {
        UPGRADE_CONFIG.put(ProductType.FIRST, new ProductUpgrade(
                new ResourceCost(25, 0, 0),    // Ресурсы для первого уровня
                new LargeNumber("5000"),        // Цена для первого уровня
                new ResourceCost(125, 0, 0),   // Ресурсы для второго уровня
                new LargeNumber("25000")        // Цена для второго уровня
        ));

        UPGRADE_CONFIG.put(ProductType.SECOND, new ProductUpgrade(
                new ResourceCost(250, 25, 0),
                new LargeNumber("555555"),
                new ResourceCost(1250, 125, 0),
                new LargeNumber("55555555")
        ));
    }

    public BakeryMenu(Player player) {
        super(player, DataBaseManager.getInstance().getBakeryDB());
        this.bakery = buildingDB.read(player.getUniqueId());
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
        MenuUtils.ItemProfitableBuildings(inventory,player,buildingDB,userDB);
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
                    Bakery bakery = buildingDB.read(player.getUniqueId());
                    if (user == null || bakery == null) {
                        player.sendMessage("Ошибка данных");
                        return;
                    }

                    handlePlayerClick(player, clickedItemType, user, bakery);
                } catch (Exception ex) {
                    player.sendMessage("Произошла ошибка");
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private void handlePlayerClick(Player player, Material clickedItemType, User user, Bakery bakery) {
        Product firstProduct = bakery.getFirstProduct();
        Product secondProduct = bakery.getSecondProduct();

        switch (clickedItemType) {
            case GREEN_STAINED_GLASS:
                handleUpIncome(user, bakery);
                break;
            case OBSIDIAN:
                handleProductUpgrade(player, firstProduct, bakery, user);
                break;
            case FEATHER:
                handleProductSell(player, firstProduct, user, bakery);
                break;
            case PAPER:
                handleProductUpgrade(player, secondProduct, bakery, user);
                break;
            case ARROW:
                handleProductSell(player, secondProduct, user, bakery);
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
        final File file = new File(Taikon.getPlugin().getDataFolder(), "schematic/bakery.schem");
        Location location = new Location(player.getWorld(), 109, 99, 136);
        WorldEditManager.paste(location, file);
    }

    @Override
    protected void startProductionTask(UUID playerId, ProductType productType) {
        BakeryProductTask task = new BakeryProductTask(playerId, (BakeryDB) buildingDB, userDB);
        Bakery bakery = buildingDB.read(playerId);

        if (bakery == null) return;

        if (productType == ProductType.FIRST) {
            task.startFirstProductTask(bakery.getFirstProduct().getTime());
        } else {
            task.startSecondProductTask(bakery.getSecondProduct().getTime());
        }
    }

}