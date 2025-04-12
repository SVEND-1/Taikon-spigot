package svend.taikon.Menu.BuildingsMenu.Settings;

import org.bukkit.entity.Player;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.Buildings.BuildingDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.BakeryMenu;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.Product;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BuildingMenu<T extends Building> extends MenuManager {
    protected final UserDB userDB;
    protected final ResourceDB resourceDB;
    protected final BuildingDB<T> buildingDB;

    public BuildingMenu(Player player, BuildingDB<T> buildingDB) {
        super(player);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        this.resourceDB = dataBaseManager.getResourceDB();
        this.buildingDB = buildingDB;
    }

    @Override
    public abstract String getMenuName();

    @Override
    public abstract int getSlots();

    @Override
    public abstract void setMenuItems();

    @Override
    public abstract void handleMenu(InventoryClickEvent e);

    // Абстрактные методы для реализации в дочерних классах
    protected abstract Map<ProductType, ProductUpgrade> getUpgradeConfig();
    protected abstract void buildBuilding(Player player);
    protected abstract void startProductionTask(UUID playerId, ProductType productType);

    // Общие методы для всех зданий
    protected void handleUpIncome(User user, T building) {
        if (user.getBalance().leftOrEqual(building.getPrice())) {
            if (building.getLevel() < 75) {
                user.setBalance(user.getBalance().subtract(building.getPrice()));
                user.setIncome(user.getIncome().add(building.getUpIncome()));

                LargeNumber upPrice = building.getPrice().divide(new LargeNumber("10"));
                building.setPrice(building.getPrice().add(upPrice));
                building.setLevel(building.getLevel() + 1);

                userDB.update(user);
                buildingDB.update(building);

                player.sendMessage("Здание улучшено до уровня " + building.getLevel());
            } else {
                player.sendMessage("Максимальный уровень достигнут");
            }
        } else {
            player.sendMessage("Недостаточно средств");
        }
    }

    protected void handleProductUpgrade(Player player, Product product, T building, User user) {
        Resource resource = resourceDB.read(player.getUniqueId());
        if (resource == null) {
            player.sendMessage("Ошибка данных ресурсов");
            return;
        }

        ProductType productType = (product == building.getFirstProduct()) ?
                ProductType.FIRST : ProductType.SECOND;
        ProductUpgrade upgrade = getUpgradeConfig().get(productType);

        if (product.getLvl() >= 2) {
            player.sendMessage("У вас максимальная прокачка");
            return;
        }

        ResourceCost cost = product.isOpen() ? upgrade.getLevel2Cost() : upgrade.getLevel1Cost();
        LargeNumber price = product.isOpen() ? upgrade.getLevel2Price() : upgrade.getLevel1Price();

        if (!hasEnoughResources(resource, cost) || !user.getBalance().leftOrEqual(price)) {
            player.sendMessage("§cНе хватает средств или ресурсов");
            return;
        }

        if (!building.isBuildingsConstructed()) {
            buildBuilding(player);
            building.setBuildingsConstructed(true);
        }

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
        buildingDB.update(building);

        player.sendMessage("§aПродукт успешно улучшен!");
    }

    protected void handleProductSell(Player player, Product product, User user, T building) {
        if (product.getCount() <= 0) {
            player.sendMessage("У вас 0 товара");
            return;
        }

        try {
            product.setCount(product.getCount() - 1);
            LargeNumber price = product.getPrice();
            user.setBalance(user.getBalance().add(price));

            userDB.update(user);
            buildingDB.update(building);

            player.sendMessage("§aПродано! +" + price);
        } catch (Exception e) {
            player.sendMessage("§cОшибка при продаже");
            e.printStackTrace();
        }
    }

    protected boolean hasEnoughResources(Resource resource, ResourceCost cost) {
        return resource.getWood() >= cost.getWood() &&
                resource.getStone() >= cost.getStone() &&
                resource.getSand() >= cost.getSand();
    }

    protected void deductResources(Resource resource, ResourceCost cost) {
        resource.setWood(resource.getWood() - cost.getWood());
        resource.setStone(resource.getStone() - cost.getStone());
        resource.setSand(resource.getSand() - cost.getSand());
    }
}