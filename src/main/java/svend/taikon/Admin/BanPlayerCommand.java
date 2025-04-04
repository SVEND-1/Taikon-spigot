package svend.taikon.Admin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class BanPlayerCommand implements CommandExecutor {
    //Todo: Бан который можно настроить там на определенное время
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        if (strings.length < 2) {
            player.sendMessage("§cИспользование: /ban <ник> <минуты> [причина]");
            return true;
        }

        Player target =  Bukkit.getPlayerExact(strings[0]);
        if (target == null) {
            player.sendMessage("§cИгрок " + strings[0] + " не найден или не в сети!");
            return true;
        }
        if(target.hasPermission("permissions.Permissions") || target.isOp()){
            player.sendMessage("Ты не можешь забанить админа");
            return true;
        }

        if(player.hasPermission("permissions.Permissions") || player.isOp()){
            try {
                long minutes = Long.parseLong(strings[1]);
                long timeBan = minutes * 60 * 1000;

                // Все слова причины в строку одну
                String reason = strings.length > 2 ?
                        String.join(" ", Arrays.copyOfRange(strings, 2, strings.length)) :
                        "Не указана";

                Date expireDate = new Date(System.currentTimeMillis() + timeBan);

                BanList banList = Bukkit.getBanList(BanList.Type.NAME);
                banList.addBan(
                        target.getName(),
                        "§cВы забанены на " + minutes + " минут\n§fПричина: " + reason,
                        expireDate,
                        null
                );

                target.kickPlayer(
                        "§cВы забанены!\n" +
                                "§fДлительность: §e" + minutes + " минут\n" +
                                "§fПричина: §e" + reason + "\n" +
                                "§fИстекает: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(expireDate)
                );

                player.sendMessage("§aИгрок " + target.getName() + " забанен на " + minutes + " минут");
            } catch (NumberFormatException e) {
                player.sendMessage("§cУкажите корректное количество минут!");
                return false;
            }
        }
        else {
            player.sendMessage("Это только для админов");
        }

        return true;
    }
}
