package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.BakeryDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Bakery;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class BakeryMenu extends MenuManager{
    private final UserDB userDB;
    private final BakeryDB bakeryDB;
    private final ConnectToMongoDB database;
    public BakeryMenu(Player player) {
        super(player);
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
        this.bakeryDB = new BakeryDB(database.getDatabase());
    }

    @Override
    public String getMenuName() {
        return "Пекарня";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        if (!e.getView().getTitle().equals(getMenuName())) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : null;

        if (clickedItemType == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                switch (clickedItemType) {
                    case GREEN_STAINED_GLASS:
                        User user = userDB.read(player.getUniqueId());
                        Bakery bakery = bakeryDB.read(player.getUniqueId());

                        user.setBalance(user.getBalance() - bakery.getPrice());
                        user.setIncome(user.getIncome() + bakery.getUpIncome());

                        int upPrice = bakery.getPrice() / 10;

                        bakery.setPrice(bakery.getPrice() + upPrice);
                        bakery.setLevel(bakery.getLevel() + 1);

                        userDB.update(user);
                        bakeryDB.update(bakery);
                        break;
                    case RED_STAINED_GLASS:
                        player.closeInventory();
                        break;
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    @Override
    public void setMenuItems() {
        MenuUtils.ItemProfitableBuildings(inventory);
    }
}
