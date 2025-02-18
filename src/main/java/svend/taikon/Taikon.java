package svend.taikon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import svend.taikon.Command.OpenMenuCommand;
import svend.taikon.Command.SpawnCommand;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Listener.BreakBlockListener;
import svend.taikon.Listener.BreakingToolsListener;
import svend.taikon.Listener.FoodLevelListener;
import svend.taikon.Listener.PlayerJoin;
import svend.taikon.NPC.NPCClickHandler;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.NPC.NPCMovementListener;
import svend.taikon.View.HologramTop;
import svend.taikon.View.ScoreboardView;

public final class Taikon extends JavaPlugin {
    private static Taikon plugin;

    public static Taikon getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        ConnectToMongoDB connectToMongoDB = new ConnectToMongoDB();

        HologramTop.create();

        NPCCreate.Create();
        NPCClickHandler.registerClickHandler(this);
        getServer().getPluginManager().registerEvents(new NPCMovementListener(), this);

        getServer().getPluginManager().registerEvents(new ScoreboardView(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelListener(), this);
        getServer().getPluginManager().registerEvents(new BreakingToolsListener(), this);


        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("menu").setExecutor(new OpenMenuCommand());
    }

    @Override
    public void onDisable() {
        HologramTop.delete();
    }
}
