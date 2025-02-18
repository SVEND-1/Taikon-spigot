package svend.taikon.Listener;

import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bson.Document;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.Taikon;
import svend.taikon.Task.AddIncomeTask;

import java.util.Optional;
import java.util.UUID;

import static svend.taikon.NPC.NPCCreate.npcs;

public class PlayerJoin implements Listener {
    private final ConnectToMongoDB database;

    public PlayerJoin() {
        this.database = new ConnectToMongoDB();
    }
    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event){
        Player player = event.getPlayer();

        startSetting(player);

        addPlayerToDataBase(player);
        addResourceInPlayerAndMongodb(player);
        NPCCreate.sendNPCsToPlayers(player);
        new AddIncomeTask(player).runTaskTimerAsynchronously(Taikon.getPlugin(),0,20);
    }

    private void startSetting(Player player){
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);
        player.setFoodLevel(25);
        player.setHealth(player.getMaxHealth());
    }

    private void addPlayerToDataBase(Player player){
        UserDB userDB = new UserDB(database.getDatabase());
        if( userDB.read(player.getUniqueId()) == null) {
            User user = new User(player.getUniqueId(), player.getDisplayName(), 0, 0);
            userDB.insert(user);
        }
    }

    private void addResourceInPlayerAndMongodb(Player player){
        ResourceDB resourceDB = new ResourceDB(database.getDatabase());
        if(resourceDB.read(player.getUniqueId()) == null){
            Resource resource = new Resource(0,0,0,0,player.getUniqueId());
            resourceDB.insert(resource);
        }
    }
}
