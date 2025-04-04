package svend.taikon;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import svend.taikon.Admin.BanPlayerCommand;
import svend.taikon.Admin.Test;
import svend.taikon.Command.OpenMenuCommand;
import svend.taikon.Command.SpawnCommand;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.Listener.*;
import svend.taikon.NPC.NPCClickHandler;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.NPC.NPCMovementListener;
import svend.taikon.Task.AddIncomeTask;
import svend.taikon.Task.UpdateHologramTask;
import svend.taikon.View.HologramTop;
import svend.taikon.View.ScoreboardView;

import java.util.HashMap;
import java.util.UUID;

public final class Taikon extends JavaPlugin {
    private static Taikon plugin;
    private ConnectToMongoDB connectToMongoDB;
    private final HashMap<UUID, AddIncomeTask> activeTasks = new HashMap<>();
    public static Taikon getPlugin() {
        return plugin;
    }
    private HologramTop hologramTop;

    @Override
    public void onEnable() {
        plugin = this;

        connectToMongoDB = new ConnectToMongoDB();

        hologramTop = new HologramTop();
        UpdateHologramTask updateTask = new UpdateHologramTask(hologramTop);

        updateTask.runTaskTimer(this, 0L, 20L * 60);

        NPCCreate.Create();
        NPCClickHandler.registerClickHandler(this);
        getServer().getPluginManager().registerEvents(new NPCMovementListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardView(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(activeTasks), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(activeTasks), this);
        getServer().getPluginManager().registerEvents(new BreakBlockListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(), this);
        getServer().getPluginManager().registerEvents(new BreakingToolsListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);

        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("menu").setExecutor(new OpenMenuCommand());
        this.getCommand("localBan").setExecutor(new BanPlayerCommand());
    }

    @Override
    public void onDisable() {
        connectToMongoDB.close();
        hologramTop.delete();
    }

}
