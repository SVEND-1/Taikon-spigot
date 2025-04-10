package svend.taikon.Listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.*;
import svend.taikon.DataBase.ModelDAO.Buildings.BakeryDB;
import svend.taikon.DataBase.ModelDAO.Buildings.GardenDB;
import svend.taikon.DataBase.ModelDAO.Buildings.RestaurantDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Buildings.Restaurant;
import svend.taikon.Model.Product;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;
import svend.taikon.NPC.NPCCreate;
import svend.taikon.Taikon;
import svend.taikon.Task.AddIncomeTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerJoinListener implements Listener {
    private final DataBaseManager dataBaseManager;
    private final UserDB userDB;
    private final ResourceDB resourceDB;
    private final BakeryDB bakeryDB;
    private final GardenDB gardenDB;
    private final RestaurantDB restaurantDB;
    private final HashMap<UUID, AddIncomeTask> activeTasks;

    public PlayerJoinListener(HashMap<UUID, AddIncomeTask> activeTasks) {
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        this.resourceDB = dataBaseManager.getResourceDB();
        this.bakeryDB = dataBaseManager.getBakeryDB();
        this.gardenDB = dataBaseManager.getGardenDB();
        this.restaurantDB = dataBaseManager.getRestaurantDB();
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

                    createDefaultBuildings(player.getUniqueId());

                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private void createDefaultBuildings(UUID playerId) {
        try {
            // Пекарня
            Product firstProductBakery = new Product("Пирожок", new LargeNumber("1000"),0, 1,500, false);
            Product secondProductBakery = new Product("Торт", new LargeNumber("12312313"),0,1,500, false);
            Bakery bakery = new Bakery("Пекарня", new LargeNumber("75"), new LargeNumber("1"), 1,
                    firstProductBakery, secondProductBakery,false, playerId);
            bakeryDB.insert(bakery);

            // Сад
            Product firstProductGarden = new Product("Роза", new LargeNumber("13123"),0, 1,500, false);
            Product secondProductGarden = new Product("Букет", new LargeNumber("312313123"),0, 1,500, false);
            Garden garden = new Garden("Сад", new LargeNumber("350"), new LargeNumber("5"), 1,
                    firstProductGarden, secondProductGarden, false,playerId);
            gardenDB.insert(garden);

            // Ресторан
            Product firstProductRestaurant = new Product("Салат", new LargeNumber("123312"),0, 1,500, false);
            Product secondProductRestaurant = new Product("Краб", new LargeNumber("1231231231"),0, 1,500, false);
            Restaurant restaurant = new Restaurant("Ресторан", new LargeNumber("1000"), new LargeNumber("10"), 1,
                    firstProductRestaurant, secondProductRestaurant,false, playerId);
            restaurantDB.insert(restaurant);
        } catch (Exception e) {

        }
    }
}