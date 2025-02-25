package svend.taikon.Utility;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuUtils {
    public static void ItemProfitableBuildings(Inventory inventory){
        ItemStack buy = createMenuItem(Material.GRAY_STAINED_GLASS,"Купить");
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
