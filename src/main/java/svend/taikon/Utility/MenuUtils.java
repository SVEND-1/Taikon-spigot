package svend.taikon.Utility;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import svend.taikon.DataBase.DAO;
import svend.taikon.DataBase.ModelDAO.Buildings.BuildingDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductType;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductUpgrade;
import svend.taikon.Menu.BuildingsMenu.Settings.ResourceCost;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.Product;
import svend.taikon.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuUtils {
    public static <T extends Building> void ItemProfitableBuildings(Inventory inventory, Player player, BuildingDB<T> buildingDB, UserDB userDB) {
        // Получаем данные из БД
        User user = userDB.read(player.getUniqueId());
        T building = buildingDB.read(player.getUniqueId());

        if (user == null || building == null) {
            player.sendMessage("Ошибка загрузки данных");
            return;
        }

        // Создаем предметы с актуальными данными
        ItemStack buy = createMenuItemWithLore(
                Material.GREEN_STAINED_GLASS,
                "Купить (Ур. " + building.getLevel() + ")",
                "Цена: " + building.getPrice().toString(),
                "Доход: +" + building.getUpIncome().toString()
        );

        Product firstProduct = building.getFirstProduct();
        ItemStack firstProductItem = createMenuItemWithLore(
                Material.OBSIDIAN,
                "Улучшить " + firstProduct.getName() + " (Ур. " + (firstProduct.isOpen() ? firstProduct.getLvl() : "0") + ")",
                "Цена: " + getUpgradePrice(building, firstProduct, true),
                "Требуется: " + getResourceCost(building, firstProduct, true)
        );

        ItemStack sellFirstProduct = createMenuItemWithLore(
                Material.FEATHER,
                "Продать " + firstProduct.getName(),
                "Цена: " + firstProduct.getPrice().toString(),
                "Доступно: " + firstProduct.getCount()
        );

        Product secondProduct = building.getSecondProduct();
        ItemStack secondProductItem = createMenuItemWithLore(
                Material.PAPER,
                "Улучшить " + secondProduct.getName() + " (Ур. " + (secondProduct.isOpen() ? secondProduct.getLvl() : "0") + ")",
                "Цена: " + getUpgradePrice(building, secondProduct, true),
                "Требуется: " + getResourceCost(building, secondProduct, true)
        );

        ItemStack sellSecondProduct = createMenuItemWithLore(
                Material.ARROW,
                "Продать " + secondProduct.getName(),
                "Цена: " + secondProduct.getPrice().toString(),
                "Доступно: " + secondProduct.getCount()
        );

        ItemStack exit = createMenuItem(Material.RED_STAINED_GLASS, "Выйти");

        inventory.setItem(9, buy);
        inventory.setItem(11, firstProductItem);
        inventory.setItem(12, sellFirstProduct);
        inventory.setItem(14, secondProductItem);
        inventory.setItem(15, sellSecondProduct);
        inventory.setItem(17, exit);
    }

    private static <T extends Building> String getUpgradePrice(T building, Product product, boolean isFirstProduct) {
        ProductUpgrade upgrade = isFirstProduct ?
                building.getUpgradeConfig().get(ProductType.FIRST) :
                building.getUpgradeConfig().get(ProductType.SECOND);

        return product.isOpen() ? upgrade.getLevel2Price().toString() : upgrade.getLevel1Price().toString();
    }

    private static <T extends Building> String getResourceCost(T building, Product product, boolean isFirstProduct) {
        ProductUpgrade upgrade = isFirstProduct ?
                building.getUpgradeConfig().get(ProductType.FIRST) :
                building.getUpgradeConfig().get(ProductType.SECOND);

        ResourceCost cost = product.isOpen() ? upgrade.getLevel2Cost() : upgrade.getLevel1Cost();
        if(product.getLvl() == 2){//мб убрать надо будет
            return "MAX";
        }
        return cost.getWood() + " дерева, " + cost.getStone() + " камня, " + cost.getSand() + " песка";
    }

    public static ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createMenuItemWithLore(Material material, String name,String price,String profit) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(price);
        lore.add(profit);

        meta.setLore(lore);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
