package svend.taikon.Donat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.User;
import svend.taikon.Taikon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BusterResource {
    public static boolean globalBoostActiveWood = false;
    public static boolean globalBoostActiveStone = false;
    public static boolean globalBoostActiveSand = false;
    private static Set<String> localBoostPlayersWood = new HashSet<>();
    private static Set<String> localBoostPlayersStone = new HashSet<>();
    private static Set<String> localBoostPlayersSand = new HashSet<>();

    public static void turnOnTheLocalBoosterWood(Player player) {//бустер на продажу
        if(player == null) return;

        if(localBoostPlayersWood.contains(player.getUniqueId().toString())) {
            player.sendMessage("Локальный бустер уже активирован");
            return;
        }

        localBoostPlayersWood.add(player.getUniqueId().toString());
        player.sendMessage("Локальный бустер дерево активирован");


        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            localBoostPlayersWood.remove(player.getUniqueId().toString());
            player.sendMessage("Локальный бустер дерево закончился");
        }, 300L);
    }

    public static void turnOnTheGlobalBoosterWood() {
        if (globalBoostActiveWood)
            return;
        else{
            globalBoostActiveWood = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер дерево активирован");
            }
        }

        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            globalBoostActiveWood = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер дерево закончился");
            }
        }, 300L);
    }

    public static boolean isPlayerInSetWood(Player player){
        return localBoostPlayersWood.contains(player.getUniqueId().toString());
    }

    public static void turnOnTheLocalBoosterStone(Player player) {
        if(player == null) return;

        if(localBoostPlayersStone.contains(player.getUniqueId().toString())) {
            player.sendMessage("Локальный бустер уже активирован");
            return;
        }

        localBoostPlayersStone.add(player.getUniqueId().toString());
        player.sendMessage("Локальный бустер камня активирован");


        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            localBoostPlayersStone.remove(player.getUniqueId().toString());
            player.sendMessage("Локальный бустер камня закончился");
        }, 300L);
    }

    public static void turnOnTheGlobalBoosterStone() {
        if (globalBoostActiveStone)
            return;
        else{
            globalBoostActiveStone = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер камня активирован");
            }
        }

        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            globalBoostActiveStone = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер камня закончился");
            }
        }, 300L);
    }

    public static boolean isPlayerInSetStone(Player player){
        return localBoostPlayersStone.contains(player.getUniqueId().toString());
    }

    public static void turnOnTheLocalBoosterSand(Player player) {
        if(player == null) return;

        if(localBoostPlayersSand.contains(player.getUniqueId().toString())) {
            player.sendMessage("Локальный бустер уже активирован");
            return;
        }

        localBoostPlayersSand.add(player.getUniqueId().toString());
        player.sendMessage("Локальный бустер песка активирован");


        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            localBoostPlayersSand.remove(player.getUniqueId().toString());
            player.sendMessage("Локальный бустер песка закончился");
        }, 300L);
    }

    public static void turnOnTheGlobalBoosterSand() {
        if (globalBoostActiveSand)
            return;
        else{
            globalBoostActiveSand = true;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер песка активирован");
            }
        }

        Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
            globalBoostActiveSand = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Глобальный бустер песка закончился");
            }
        }, 300L);
    }
    public static boolean isPlayerInSetSand(Player player){
        return localBoostPlayersSand.contains(player.getUniqueId().toString());
    }
}
