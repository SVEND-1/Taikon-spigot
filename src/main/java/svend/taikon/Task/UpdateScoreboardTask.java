package svend.taikon.Task;


import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;

public class UpdateScoreboardTask extends BukkitRunnable {

    private final Player player;
    private final UserDB userDB;
    private final ResourceDB resourceDB;

    public UpdateScoreboardTask(Player player, UserDB userDB,ResourceDB resourceDB) {
        this.player = player;
        this.userDB = userDB;
        this.resourceDB = resourceDB;
    }

    @Override
    public void run() {
        User user = userDB.read(player.getUniqueId());
        Resource resource = resourceDB.read(player.getUniqueId());
        if (user != null) {
            updateScoreboard(player, user,resource);
        }
    }

    private void updateScoreboard(Player player, User user, Resource resource) {
        Scoreboard playerScoreboard = player.getScoreboard();

        Objective oldObjective = playerScoreboard.getObjective("scoreboard");
        if (oldObjective != null) {
            oldObjective.unregister();
        }

        Objective objective = playerScoreboard.registerNewObjective(
                "scoreboard",
                "dummy",
                "Taikon"
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Добавляем обновленные записи
        objective.getScore("§aИмя: §e" + user.getName()).setScore(8);
        objective.getScore("§bДоход: §e" + user.getActualIncome()).setScore(7);
        objective.getScore("§cБаланс: §e" + user.getBalance()).setScore(6);
        objective.getScore(" ").setScore(5);
        objective.getScore("Цветов: " + resource.getFlowers()).setScore(4);
        objective.getScore("Дерево: " + resource.getWood()).setScore(3);
        objective.getScore("Камня: " + resource.getStone()).setScore(2);
        objective.getScore("Песка: " + resource.getSand()).setScore(1);
    }
}

