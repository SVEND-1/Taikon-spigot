package svend.taikon.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminUtils {

    public static final int PAGE_SIZE = 45;
    public static final int PREV_PAGE_SLOT = 45;
    public static final int NEXT_PAGE_SLOT = 53;
    public static final int INFO_SLOT = 49;

    public static boolean isAdmin(Player player) {
        return player.hasPermission("permissions.Permissions") || player.isOp();
    }

    public static boolean isTargetAdmin(Player target) {
        return target.hasPermission("permissions.Permissions") || target.isOp();
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setDisplayName("§e" + player.getName());
        meta.setOwningPlayer(player);

        List<String> lore = new ArrayList<>();
        lore.add("§7ID: " + player.getUniqueId());
        lore.add("§7Уровень: §a" + player.getLevel());
        lore.add("§7Здоровье: §c" + (int) player.getHealth() + "§7/§c" + (int) player.getMaxHealth());
        lore.add("§7Пинг: §a" + player.getPing() + "мс");
        meta.setLore(lore);

        head.setItemMeta(meta);
        return head;
    }

    public static void setupPagination(Inventory inventory, int currentPage, int totalPages) {
        if (currentPage > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();
            prevMeta.setDisplayName("§aПредыдущая страница");
            prev.setItemMeta(prevMeta);
            inventory.setItem(PREV_PAGE_SLOT, prev);
        }

        if (currentPage < totalPages - 1) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName("§aСледующая страница");
            next.setItemMeta(nextMeta);
            inventory.setItem(NEXT_PAGE_SLOT, next);
        }

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§fСтраница §e" + (currentPage + 1) + "§f/§e" + totalPages);
        info.setItemMeta(infoMeta);
        inventory.setItem(INFO_SLOT, info);
    }
}