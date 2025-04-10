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

public class BakeryMenu extends MenuManager {
    private final UserDB userDB;
    private final BakeryDB bakeryDB;
    private final ResourceDB resourceDB;
    private Bakery bakery;

    // улучшений продуктов
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
        super(player);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        this.bakeryDB = dataBaseManager.getBakeryDB();
        this.resourceDB = dataBaseManager.getResourceDB();
        this.bakery = bakeryDB.read(player.getUniqueId());
    }

    @Override
    public String getMenuName() {
        return "Пекарня";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        if (!e.getView().getTitle().equals(getMenuName())) return;

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : null;
        if (clickedItemType == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    User user = userDB.read(player.getUniqueId());
                    Bakery bakery = bakeryDB.read(player.getUniqueId());
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
                handleUpIncome(user,bakery);
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
    public void setMenuItems() {
        MenuUtils.ItemProfitableBuildings(inventory, bakery);
    }

    private void handleUpIncome(User user,Bakery bakery){
        if (user.getBalance().leftOrEqual(bakery.getPrice())) {
            if (bakery.getLevel() < 75) {
                user.setBalance(user.getBalance().subtract(bakery.getPrice()));
                user.setIncome(user.getIncome().add(bakery.getUpIncome()));

                LargeNumber upPrice = bakery.getPrice().divide(new LargeNumber("10"));//начальную стоимость делим на ratio и прибавляем к начальной
                bakery.setPrice(bakery.getPrice().add(upPrice));
                bakery.setLevel(bakery.getLevel() + 1);

                userDB.update(user);
                bakeryDB.update(bakery);

                player.sendMessage("Здание улучшено до уровня " + bakery.getLevel());
            } else {
                player.sendMessage("Максимальный уровень достигнут");
            }
        } else {
            player.sendMessage("Недостаточно средств");
        }
    }

    private void handleProductUpgrade(Player player, Product product, Bakery bakery, User user) {
        Resource resource = resourceDB.read(player.getUniqueId());
        if (resource == null) {
            player.sendMessage("Ошибка данных ресурсов");
            return;
        }

        ProductType productType = (product == bakery.getFirstProduct()) ?
                ProductType.FIRST : ProductType.SECOND;
        ProductUpgrade upgrade = UPGRADE_CONFIG.get(productType);

        if (product.getLvl() >= 2) {
            player.sendMessage("У вас максимальная прокачка");
            return;
        }

        // требования для текущего уровня
        ResourceCost cost = product.isOpen() ? upgrade.level2Cost : upgrade.level1Cost;
        LargeNumber price = product.isOpen() ? upgrade.level2Price : upgrade.level1Price;

        if (!hasEnoughResources(resource, cost) || !user.getBalance().leftOrEqual(price)) {
            player.sendMessage("§cНе хватает средств или ресурсов");
            return;
        }

        if (!bakery.isBuildingsConstructed()) {
            buildBakery(player);
            bakery.setBuildingsConstructed(true);
        }

        // Списание ресурсов
        deductResources(resource, cost);
        user.setBalance(user.getBalance().subtract(price));

        if (product.isOpen()) {
            product.setLvl(2);
        } else {
            product.setOpen(true);
            startProductionTask(player.getUniqueId(), productType);
        }

        userDB.update(user);
        resourceDB.update(resource);
        bakeryDB.update(bakery);

        player.sendMessage("§aПродукт успешно улучшен!");
    }

    private void buildBakery(Player player) {
        final File file = new File(Taikon.getPlugin().getDataFolder(), "schematic/bakery.schem");
        Location location = new Location(player.getWorld(), 109, 99, 136);
        WorldEditManager.paste(location, file);
    }

    private void startProductionTask(UUID playerId, ProductType productType) {
        BakeryProductTask task = new BakeryProductTask(playerId, bakeryDB, userDB);
        if (productType == ProductType.FIRST) {
            task.startFirstProductTask(bakery.getFirstProduct().getTime());
        } else {
            task.startSecondProductTask(bakery.getSecondProduct().getTime());
        }
    }

    private boolean hasEnoughResources(Resource resource, ResourceCost cost) {
        return resource.getWood() >= cost.wood &&
                resource.getStone() >= cost.stone &&
                resource.getSand() >= cost.sand;
    }

    private void deductResources(Resource resource, ResourceCost cost) {
        resource.setWood(resource.getWood() - cost.wood);
        resource.setStone(resource.getStone() - cost.stone);
        resource.setSand(resource.getSand() - cost.sand);
    }

    private void handleProductSell(Player player, Product product, User user, Bakery bakery) {
        if (product.getCount() <= 0) {
            player.sendMessage("У вас 0 товара");
            return;
        }

        try {
            product.setCount(product.getCount() - 1);
            LargeNumber price = product.getPrice();
            user.setBalance(user.getBalance().add(price));

            userDB.update(user);
            bakeryDB.update(bakery);

            player.sendMessage("§aПродано! +" + price);
        } catch (Exception e) {
            player.sendMessage("§cОшибка при продаже");
            e.printStackTrace();
        }
    }

    private enum ProductType { FIRST, SECOND }

    private static class ProductUpgrade {
        final ResourceCost level1Cost;
        final LargeNumber level1Price;
        final ResourceCost level2Cost;
        final LargeNumber level2Price;

        ProductUpgrade(ResourceCost level1Cost, LargeNumber level1Price,
                       ResourceCost level2Cost, LargeNumber level2Price) {
            this.level1Cost = level1Cost;
            this.level1Price = level1Price;
            this.level2Cost = level2Cost;
            this.level2Price = level2Price;
        }
    }

    private static class ResourceCost {
        final int wood;
        final int stone;
        final int sand;

        ResourceCost(int wood, int stone, int sand) {
            this.wood = wood;
            this.stone = stone;
            this.sand = sand;
        }
    }
}