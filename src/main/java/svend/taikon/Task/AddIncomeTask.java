package svend.taikon.Task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.User;

public class AddIncomeTask extends BukkitRunnable {
    private final Player player;
    private final ConnectToMongoDB database;
    private final UserDB userDB;
    public AddIncomeTask(Player player){
        this.player = player;
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
    }
    @Override
    public void run() {
        User user = userDB.read(player.getUniqueId());

        user.setBalance(user.getBalance() + user.getIncome());
        userDB.update(user);

    }
}
