package svend.taikon;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import svend.taikon.Command.OpenMenuCommand;
import svend.taikon.Command.SpawnCommand;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.Listener.*;
import svend.taikon.NPC.NPCClickHandler;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.NPC.NPCMovementListener;
import svend.taikon.Task.AddIncomeTask;
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


    @Override
    public void onEnable() {
        plugin = this;

        connectToMongoDB = new ConnectToMongoDB();

        HologramTop.create();

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
    }

    @Override
    public void onDisable() {
        connectToMongoDB.close();
        HologramTop.delete();
    }

}
