package svend.taikon.Utility;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import svend.taikon.DataBase.DAO;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuUtils {
    public static <T extends Building> void handleBuildingUpgrade(Player player, T building, DAO<T, UUID> buildingDB, User user, UserDB userDB, LargeNumber ratio) {
        if (user.getBalance().leftOrEqual(building.getPrice())) {
            if (building.getLevel() < 75) {
                user.setBalance(user.getBalance().subtract(building.getPrice()));
                user.setIncome(user.getIncome().add(building.getUpIncome()));

                LargeNumber upPrice = building.getPrice().divide(ratio);//начальную стоимость делим на ratio и прибавляем к начальной
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

    public static void ItemProfitableBuildings(Inventory inventory,Building building){
        ItemStack buy = createMenuItemWithLore(Material.GREEN_STAINED_GLASS,"Купить","Цена: "  ,"Доход: " );
        ItemStack firstProduct = createMenuItemWithLore(Material.OBSIDIAN,"Улучшить первого продукта","Цена: " ,"");
        ItemStack sellFirstProduct = createMenuItemWithLore(Material.FEATHER,"Продать один продукт","Цена: " ,"");
        ItemStack secondProduct = createMenuItemWithLore(Material.PAPER,"Улучшить второго продукт","Цена: " ,"");
        ItemStack sellSecondProduct = createMenuItemWithLore(Material.ARROW,"Продать один продукт","Цена: " ,"");
        ItemStack exit = createMenuItem(Material.RED_STAINED_GLASS,"Выйти");

        inventory.setItem(9,buy);
        inventory.setItem(11,firstProduct);
        inventory.setItem(12,sellFirstProduct);
        inventory.setItem(14,secondProduct);
        inventory.setItem(15,sellSecondProduct);
        inventory.setItem(17,exit);
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
