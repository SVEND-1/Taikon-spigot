package svend.taikon.Listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.*;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Buildings.Restaurant;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.Taikon;
import svend.taikon.Task.AddIncomeTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final UserDB userDB;
    private final ResourceDB resourceDB;
    private final BakeryDB bakeryDB;
    private final GardenDB gardenDB;
    private final RestaurantDB restaurantDB;
    private final HashMap<UUID, AddIncomeTask> activeTasks;

    public PlayerJoinListener(HashMap<UUID, AddIncomeTask> activeTasks) {
        ConnectToMongoDB database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
        this.resourceDB = new ResourceDB(database.getDatabase());
        this.bakeryDB = new BakeryDB(database.getDatabase());
        this.gardenDB = new GardenDB(database.getDatabase());
        this.restaurantDB = new RestaurantDB(database.getDatabase());
        this.activeTasks = activeTasks;
    }

    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        startSetting(player);
        addPlayerToDataBase(player);
        NPCCreate.sendNPCsToPlayers(player);

        event.setJoinMessage(null);

        if (!activeTasks.containsKey(player.getUniqueId())) {
            AddIncomeTask task = new AddIncomeTask(player);
            activeTasks.put(player.getUniqueId(), task);
            task.runTaskTimerAsynchronously(Taikon.getPlugin(), 0, 20);
        }
    }
    private void startSetting(Player player) {
        //Todo:дать серивезованный инвентарь
        player.setGameMode(GameMode.SURVIVAL);
        player.setInvulnerable(true);
        player.setFoodLevel(25);
        player.setHealth(player.getMaxHealth());
    }

    private void addPlayerToDataBase(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (userDB.read(player.getUniqueId()) == null) {
                    User user = new User(player.getUniqueId(), player.getDisplayName(), new LargeNumber("0"), new LargeNumber("0"),1);
                    userDB.insert(user);

                    Resource resource = new Resource(0, 0, 0, 0, player.getUniqueId());
                    resourceDB.insert(resource);

                    Bakery bakery = new Bakery("Пекарня",new LargeNumber("75"),new LargeNumber("1"),1,player.getUniqueId());
                    bakeryDB.insert(bakery);

                    Garden garden = new Garden("Сад", new LargeNumber("350"),new LargeNumber("5"),1,player.getUniqueId());
                    gardenDB.insert(garden);

                    Restaurant restaurant = new Restaurant("Ресторан",new LargeNumber("1000"),new LargeNumber("10"),1,player.getUniqueId());
                    restaurantDB.insert(restaurant);
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }
}