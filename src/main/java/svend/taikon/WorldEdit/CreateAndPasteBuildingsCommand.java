package svend.taikon.WorldEdit;

import net.minecraft.util.Tuple;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import svend.taikon.Taikon;
import svend.taikon.Utility.AdminUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateAndPasteBuildingsCommand implements CommandExecutor {

    private final Map<UUID,Tuple<Location,Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player))
            return true;


        Player player = (Player) commandSender;
        String param = strings[0].toLowerCase();

        if(!AdminUtils.isAdmin(player)){
            player.sendMessage("Это только для админов");
            return true;
        }

        final Tuple<Location,Location> selection = selections.getOrDefault(player.getUniqueId(),new Tuple<>(null,null));

        if("pos1".equals(param)){
            selection.setA(player.getLocation());
            player.sendMessage("Установлено");
            selections.put(player.getUniqueId(),selection);
        }
        else if("pos2".equals(param)){
            selection.setB(player.getLocation());
            player.sendMessage("Установлено");
            selections.put(player.getUniqueId(),selection);
        }
        else if("save".equals(param)){
            if(selection.getA() == null|| selection.getB() == null){
                player.sendMessage("Не все точки указаны");
                return true;
            }
            if(strings.length != 2){
                player.sendMessage("/schematic save <name>");
                return true;
            }

            final File location = new File(Taikon.getPlugin().getDataFolder(),"schematic/" + strings[1] + ".schem");

            if(!location.getParentFile().exists())
                location.getParentFile().mkdirs();

            WorldEditManager.save(selection.getA(), selection.getB(), location);
            player.sendMessage("Сохранено");
        } else if ("paste".equals(param)) {
            if(strings.length != 2){
                player.sendMessage("/schematic save <name>");
                return true;
            }

            final File file = new File(Taikon.getPlugin().getDataFolder(),"schematic/" + strings[1] + ".schem");

            if(!file.exists()){
                player.sendMessage("Такой схемы нету");

                return true;
            }

            WorldEditManager.paste(player.getLocation(),file);
            player.sendMessage("Урааа");
        }

        return true;
    }
}
