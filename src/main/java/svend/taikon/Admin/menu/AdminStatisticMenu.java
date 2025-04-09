package svend.taikon.Admin.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.User;
import svend.taikon.Utility.AdminUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminStatisticMenu extends MenuManager {
    private int currentPage;
    private final UserDB userDB;
    private final ConnectToMongoDB database;

    public AdminStatisticMenu(Player player) {
        super(player);
        this.currentPage = 0;
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
    }

    @Override
    public String getMenuName() {
        return "§6Онлайн игроки §7(Стр. " + (currentPage + 1) + ")";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();

        if (!AdminUtils.isAdmin(player)) {
            player.sendMessage("§cЭто только для админов");
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;

        switch (e.getSlot()) {
            case AdminUtils.PREV_PAGE_SLOT:
                if (clickedItem.getType() == Material.ARROW) {
                    currentPage--;
                    open();
                }
                break;
            case AdminUtils.NEXT_PAGE_SLOT:
                if (clickedItem.getType() == Material.ARROW) {
                    currentPage++;
                    open();
                }
                break;
            default:
                if (clickedItem.getType() == Material.PLAYER_HEAD) {
                    SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
                    if (meta != null && meta.getOwningPlayer() != null) {
                        Player target = meta.getOwningPlayer().getPlayer();
                        if (target != null) {

                        }
                    }
                }
                break;
        }
    }

    public ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setDisplayName("§e" + player.getName());
        meta.setOwningPlayer(player);

        User user = userDB.read(player.getUniqueId());

        List<String> lore = new ArrayList<>();
        lore.add("§7ID: " + player.getUniqueId());
        lore.add("§7Баланс: §a" + user.getBalance());
        lore.add("§7Доход: §c" + user.getIncome());
        lore.add("§7Пинг: §a" + player.getPing() + "мс");
        meta.setLore(lore);

        head.setItemMeta(meta);
        return head;
    }
    @Override
    public void setMenuItems() {
        inventory.clear();
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

        int totalPages = (int) Math.ceil((double) onlinePlayers.size() / AdminUtils.PAGE_SIZE);
        currentPage = Math.max(0, Math.min(currentPage, totalPages - 1));

        int start = currentPage * AdminUtils.PAGE_SIZE;
        int end = Math.min(start + AdminUtils.PAGE_SIZE, onlinePlayers.size());

        for (int i = start; i < end; i++) {
            inventory.addItem(getPlayerHead(onlinePlayers.get(i)));
        }

        AdminUtils.setupPagination(inventory, currentPage, totalPages);
    }
}
