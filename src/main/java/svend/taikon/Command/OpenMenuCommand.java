package svend.taikon.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import svend.taikon.Menu.BuildingsMenu.BakeryMenu;
import svend.taikon.Menu.BuildingsMenu.GardenMenu;
import svend.taikon.Menu.BuildingsMenu.RestaurantMenu;
import svend.taikon.Menu.DonatMenu;
import svend.taikon.Menu.SellResourceMenu;
import svend.taikon.Menu.ToolsMenu;

public class OpenMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(strings[0].equalsIgnoreCase("Tools")){
                ToolsMenu toolsMenu = new ToolsMenu(player);
                toolsMenu.open();
            }
            else if(strings[0].equalsIgnoreCase("Donat")){
                DonatMenu donatMenu = new DonatMenu(player);
                donatMenu.open();
            }
            else if(strings[0].equalsIgnoreCase("Sell")){
                SellResourceMenu sellResourceMenu = new SellResourceMenu(player);
                sellResourceMenu.open();
            }
            else if(strings[0].equalsIgnoreCase("Bakery")){
                BakeryMenu bakeryMenu = new BakeryMenu(player);
                bakeryMenu.open();
            }
            else if(strings[0].equalsIgnoreCase("Garden")){
                GardenMenu gardenMenu = new GardenMenu(player);
                gardenMenu.open();
            }
            else if(strings[0].equalsIgnoreCase("Restaurant")){
                RestaurantMenu restaurantMenu = new RestaurantMenu(player);
                restaurantMenu.open();
            }
        }

        return true;
    }
}
