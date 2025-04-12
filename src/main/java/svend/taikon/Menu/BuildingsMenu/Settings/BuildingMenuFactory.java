package svend.taikon.Menu.BuildingsMenu.Settings;

import org.bukkit.entity.Player;
import svend.taikon.Menu.BuildingsMenu.BakeryMenu;
import svend.taikon.Menu.BuildingsMenu.GardenMenu;
import svend.taikon.Menu.BuildingsMenu.RestaurantMenu;
import svend.taikon.Menu.MenuManager;

public class BuildingMenuFactory {
    public static MenuManager createMenu(Player player, BuildingType type) {
        switch (type) {
            case BAKERY:
                return new BakeryMenu(player);
            case GARDEN:
                return new GardenMenu(player);
            case RESTAURANT:
                return new RestaurantMenu(player);
            default:
                throw new IllegalArgumentException("Unknown building type: " + type);
        }
    }
}
