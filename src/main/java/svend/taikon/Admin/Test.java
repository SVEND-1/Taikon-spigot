package svend.taikon.Admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import svend.taikon.Menu.MenuManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Test extends MenuManager {
    private static final int PAGE_SIZE = 45; // 9*4 строки для блоков
    private static final int PREV_PAGE_SLOT = 45; // Слот для кнопки "Назад"
    private static final int NEXT_PAGE_SLOT = 53; // Слот для кнопки "Вперед"
    private static final int INFO_SLOT = 49; // Слот для информации о странице

    private int currentPage;

    public Test(Player player) {
        super(player);
        this.currentPage = 0;
    }

    @Override
    public String getMenuName() {
        return "§6Список блоков §7(Стр. " + (currentPage + 1) + ")";
    }

    @Override
    public int getSlots() {
        return 54; // 6 рядов инвентаря
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null) return;

        switch (e.getSlot()) {
            case PREV_PAGE_SLOT:
                if (clickedItem.getType() == Material.ARROW) {
                    currentPage--;
                    open();
                }
                break;
            case NEXT_PAGE_SLOT:
                if (clickedItem.getType() == Material.ARROW) {
                    currentPage++;
                    open();
                }
                break;
            default:
                if (clickedItem.getType().isBlock()) {
                    player.sendMessage("§aВыбран блок: §e" + clickedItem.getType().name());
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.clear(); // Очищаем инвентарь перед заполнением

        // Получаем все блоки Minecraft (с фильтрацией)
        List<Material> allBlocks = Arrays.stream(Material.values())
                .filter(m -> m.isBlock() && m.isItem() && !m.name().contains("LEGACY"))
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) allBlocks.size() / PAGE_SIZE);
        currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

        // Добавляем блоки для текущей страницы
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, allBlocks.size());

        for (int i = start; i < end; i++) {
            Material block = allBlocks.get(i);
            try {
                ItemStack item = new ItemStack(block);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName("§e" + block.name());
                    item.setItemMeta(meta);
                    inventory.addItem(item);
                }
            } catch (Exception ignored) {
                // Пропускаем блоки, которые нельзя создать как ItemStack
            }
        }

        // Добавляем кнопку "Назад"
        if (currentPage > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();
            prevMeta.setDisplayName("§aПредыдущая страница");
            prev.setItemMeta(prevMeta);
            inventory.setItem(PREV_PAGE_SLOT, prev);
        }

        // Добавляем кнопку "Вперед"
        if (currentPage < totalPages - 1) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.setDisplayName("§aСледующая страница");
            next.setItemMeta(nextMeta);
            inventory.setItem(NEXT_PAGE_SLOT, next);
        }

        // Добавляем информацию о странице
        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName("§fСтраница §e" + (currentPage + 1) + "§f/§e" + totalPages);
        info.setItemMeta(infoMeta);
        inventory.setItem(INFO_SLOT, info);
    }
}