package svend.taikon.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import svend.taikon.Menu.ToolsMenu;

public class OpenMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            ToolsMenu toolsMenu = new ToolsMenu(player);

            if(strings[0].equalsIgnoreCase("Tools")){
                toolsMenu.open();
            }
        }

        return true;
    }
}
