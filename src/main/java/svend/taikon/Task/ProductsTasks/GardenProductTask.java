package svend.taikon.Task.ProductsTasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ModelDAO.Buildings.BuildingDB;
import svend.taikon.DataBase.ModelDAO.Buildings.GardenDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Product;
import svend.taikon.Taikon;

import java.util.UUID;

public class GardenProductTask {

    private final UUID playerId;
    private final BuildingDB<Garden> gardenDB;
    private final UserDB userDB;
    private boolean isRunning = false;
    private BukkitRunnable firstProductTask;
    private BukkitRunnable secondProductTask;

    public GardenProductTask(UUID playerId, BuildingDB<Garden> gardenDB, UserDB userDB) {
        this.playerId = playerId;
        this.gardenDB = gardenDB;
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
                    Garden garden = gardenDB.read(playerId);
                    if (garden == null) {
                        cancel();
                        return;
                    }

                    Product first = garden.getFirstProduct();
                    if (first != null && first.isOpen()) {
                        first.setCount(first.getCount() + 1);
                        gardenDB.update(garden);

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
                    Garden garden = gardenDB.read(playerId);
                    if (garden == null) {
                        cancel();
                        return;
                    }

                    Product second = garden.getSecondProduct();
                    if (second != null && second.isOpen()) {
                        second.setCount(second.getCount() + 1);
                        gardenDB.update(garden);
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