package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.BakeryDB;
import svend.taikon.DataBase.ModelDAO.GardenDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class GardenMenu extends MenuManager {
    private final UserDB userDB;
    private final GardenDB gardenDB;
    private final ConnectToMongoDB database;
    public GardenMenu(Player player) {
        super(player);
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
        this.gardenDB = new GardenDB(database.getDatabase());
    }

    @Override
    public String getMenuName() {
        return "Сад";
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
                        Garden garden = gardenDB.read(player.getUniqueId());

                        MenuUtils.handleBuildingUpgrade(player,garden,gardenDB,user,userDB,7);

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
