package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.Buildings.RestaurantDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Restaurant;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.MenuUtils;

public class RestaurantMenu extends MenuManager {
    private final UserDB userDB;
    private final RestaurantDB restaurantDB;
    private final DataBaseManager dataBaseManager;
    private Restaurant restaurant;
    public RestaurantMenu(Player player) {
        super(player);
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        this.restaurantDB = dataBaseManager.getRestaurantDB();
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
                        Restaurant restaurant = restaurantDB.read(player.getUniqueId());

                        MenuUtils.handleBuildingUpgrade(player,restaurant,restaurantDB,user,userDB,new LargeNumber("5"));

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
        MenuUtils.ItemProfitableBuildings(inventory,restaurant);
    }
}
