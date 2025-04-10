package svend.taikon.Task.ProductsTasks;

import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ModelDAO.Buildings.BakeryDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Product;
import svend.taikon.Taikon;

import java.util.UUID;

public class BakeryProductTask{
    private final UUID playerId;
    private final BakeryDB bakeryDB;
    private final UserDB userDB;
    private boolean isRunning = false;
    private BukkitRunnable firstProductTask;
    private BukkitRunnable secondProductTask;

    public BakeryProductTask(UUID playerId, BakeryDB bakeryDB, UserDB userDB) {
        this.playerId = playerId;
        this.bakeryDB = bakeryDB;
        this.userDB = userDB;
    }

    public void startFirstProductTask(int intervalTicks) {
        if (firstProductTask != null) {
            firstProductTask.cancel();
        }

        firstProductTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) return;
                isRunning = true;

                try {
                    Bakery bakery = bakeryDB.read(playerId);
                    if (bakery == null) {
                        cancel();
                        return;
                    }

                    Product first = bakery.getFirstProduct();
                    if (first != null && first.isOpen()) {
                        first.setCount(first.getCount() + 1);
                        bakeryDB.update(bakery);

                    }
                } finally {
                    isRunning = false;
                }

                if (!Bukkit.getPlayer(playerId).isOnline()) {
                    cancel();
                }
            }
        };
        firstProductTask.runTaskTimerAsynchronously(Taikon.getPlugin(), 0, intervalTicks);
    }



    public void startSecondProductTask(int intervalTicks) {
        if (secondProductTask != null) {
            secondProductTask.cancel();
        }

        secondProductTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) return;
                isRunning = true;

                try {
                    Bakery bakery = bakeryDB.read(playerId);
                    if (bakery == null) {
                        cancel();
                        return;
                    }

                    Product second = bakery.getSecondProduct();
                    if (second != null && second.isOpen()) {
                        second.setCount(second.getCount() + 1);
                        bakeryDB.update(bakery);
                    }
                } finally {
                    isRunning = false;
                }

                if (!Bukkit.getPlayer(playerId).isOnline()) {
                    cancel();
                }
            }
        };
        secondProductTask.runTaskTimerAsynchronously(Taikon.getPlugin(), 0, intervalTicks);
    }
}
