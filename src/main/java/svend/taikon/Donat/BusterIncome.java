package svend.taikon.Donat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.User;
import svend.taikon.Taikon;

public class BusterIncome {
    private final UserDB userDB;
    private static boolean globalBoostActive = false;

    public BusterIncome(UserDB userDB) {
        this.userDB = userDB;
    }

    public void turnOnTheLocalBooster(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                User user = userDB.read(player.getUniqueId());
                if (user == null) return;


                int currentMultiplier = (int) user.getIncomeMultiplier();

                if (currentMultiplier >= (globalBoostActive ? 4 : 2)) {
                    player.sendMessage("§cЛокальный бустер уже активирован");
                    return;
                }

                user.setIncomeMultiplier(currentMultiplier * 2);
                userDB.update(user);
                player.sendMessage("Ваш доход увеличился на x2");

                // Запланировать отключение
                Bukkit.getScheduler().runTaskLater(Taikon.getPlugin(), () -> {
                    User updatedUser = userDB.read(player.getUniqueId());
                    if (updatedUser != null && updatedUser.getIncomeMultiplier() > 1) {
                        updatedUser.setIncomeMultiplier(updatedUser.getIncomeMultiplier() / 2);
                        userDB.update(updatedUser);
                        player.sendMessage("§cВаш локальный буст дохода закончился");
                    }
                }, 300L);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }


    public void turnOnTheGlobalBooster() {
        if (globalBoostActive) {
            Bukkit.broadcastMessage("§cГлобальный буст уже активен!");
            return;
        }

        globalBoostActive = true;
        Bukkit.broadcastMessage("§aГлобальный буст дохода активирован на 1 час!");

        new BukkitRunnable() {
            @Override
            public void run() {
                // Применить буст всем онлайн игрокам
                for (Player player : Bukkit.getOnlinePlayers()) {
                    User user = userDB.read(player.getUniqueId());

                    if(user.getIncomeMultiplier() >= 4){
                        player.sendMessage("Бустер уже активирован");
                        return;
                    }


                    if (user != null) {
                        user.setIncomeMultiplier(user.getIncomeMultiplier() * 2);
                        userDB.update(user);

                        // Отправляем сообщение в главном потоке
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage("§aВаш доход увеличен в 2 раза на 1 час!");
                            }
                        }.runTask(Taikon.getPlugin());
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        resetGlobalBoost();
                    }
                }.runTaskLater(Taikon.getPlugin(), 300L);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private void resetGlobalBoost() {
        if (!globalBoostActive) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = userDB.read(player.getUniqueId());
            if (user != null) {
                user.setIncomeMultiplier(user.getIncomeMultiplier() / 2);
                userDB.update(user);
            }
        }

        globalBoostActive = false;
        Bukkit.broadcastMessage("§cГлобальный буст дохода закончился");
    }

}
