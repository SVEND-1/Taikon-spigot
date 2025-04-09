package svend.taikon.Task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.BakeryDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Product;

import java.util.UUID;

public class ProductTask extends BukkitRunnable {
    private final UUID playerId;
    private final BakeryDB bakeryDB;
    private final UserDB userDB;
    private boolean isRunning = false;

    public ProductTask(UUID playerId, BakeryDB bakeryDB, UserDB userDB) {
        this.playerId = playerId;
        this.bakeryDB = bakeryDB;
        this.userDB = userDB;
    }

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
            Product second = bakery.getSecondProduct();
            boolean needsUpdate = false;

            if (first != null && first.isOpen()) {
                first.setCount(first.getCount() + 1);
                needsUpdate = true;
            }

            if (second != null && second.isOpen()) {
                second.setCount(second.getCount() + 1);
                needsUpdate = true;
            }

            if (needsUpdate) {
                bakeryDB.update(bakery);
            }
        } finally {
            isRunning = false;
        }
    }
}
