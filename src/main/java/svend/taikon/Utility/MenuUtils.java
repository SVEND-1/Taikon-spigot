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

import java.util.UUID;

public class MenuUtils {
    public static <T extends Building> void handleBuildingUpgrade(Player player, T building, DAO<T, UUID> buildingDB, User user, UserDB userDB, LargeNumber ratio) {
        if (user.getBalance().leftOrEqual(building.getPrice())) {
            if (building.getLevel() < 75) {
                user.setBalance(user.getBalance().subtract(building.getPrice()));
                user.setIncome(user.getIncome().add(building.getUpIncome()));

                LargeNumber upPrice = building.getPrice().divide(ratio);
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

    public static void ItemProfitableBuildings(Inventory inventory){
        ItemStack buy = createMenuItem(Material.GREEN_STAINED_GLASS,"Купить");
        ItemStack exit = createMenuItem(Material.RED_STAINED_GLASS,"Выйти");

        inventory.setItem(2,buy);
        inventory.setItem(6,exit);
    }

    public static ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
