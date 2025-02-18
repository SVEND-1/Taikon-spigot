package svend.taikon.View;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Taikon;
import svend.taikon.Task.UpdateScoreboardTask;

public class ScoreboardView implements Listener {
    private final UserDB userDB;
    private final ResourceDB resourceDB;
    private final ConnectToMongoDB database;
    public ScoreboardView() {
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
        this.resourceDB = new ResourceDB(database.getDatabase());
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);

        new UpdateScoreboardTask(player, userDB,resourceDB)
                .runTaskTimerAsynchronously(Taikon.getPlugin(), 0, 20);
    }
}
