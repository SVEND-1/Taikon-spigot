package svend.taikon.View;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.Model.User;


import java.util.ArrayList;
import java.util.List;


public class HologramTop {
    private final UserDB userDB;
    private final ConnectToMongoDB database;

    public HologramTop() {
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
    }

    public List<ArmorStand> create() {
        Location location = new Location(Bukkit.getWorld("world"), 100, 100, 150);
        List<ArmorStand> stands = new ArrayList<>();
        List<User> users = userDB.top10();

        if (users.isEmpty()) {
            return stands;
        }

        ArmorStand topStand = createHologram(location, "Топ");
        if (topStand != null) {
            stands.add(topStand);
        }

        location.add(0, -0.3, 0);

        for (int i = 0; i < 10; i++) {
            String hologramText;
            if (i < users.size()) {
                User user = users.get(i);
                hologramText = (i + 1) + " " + user.getName() + "     " + "Баланс:" + user.getBalance();
            } else {
                hologramText = (i + 1) + " Место: Нет данных";
            }
            ArmorStand stand = createHologram(location, hologramText);
            if (stand != null) {
                stands.add(stand);
            }
            location.add(0, -0.3, 0);
        }

        return stands;
    }

    public void delete(){
        for(ArmorStand stand : create()){
            stand.remove();
        }
    }
    private static ArmorStand createHologram(Location location, String text) {
        ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setCustomNameVisible(true);
        hologram.setCustomName(text);
        hologram.setGravity(false);
        return hologram;
    }
}
