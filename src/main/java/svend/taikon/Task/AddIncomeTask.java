package svend.taikon.Task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.User;


public class AddIncomeTask extends BukkitRunnable {
    private final Player player;
    private final DataBaseManager dataBaseManager;
    private final UserDB userDB;
    public AddIncomeTask(Player player){
        this.player = player;
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
    }
    @Override
    public void run() {
       User user = userDB.read(player.getUniqueId());

        user.setBalance(user.getBalance().add(user.getIncome()
                .multiply(new LargeNumber(String.valueOf(user.getIncomeMultiplier())))));
        userDB.update(user);

    }
}
