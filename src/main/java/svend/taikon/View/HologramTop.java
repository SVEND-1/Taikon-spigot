package svend.taikon.View;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;


import java.util.ArrayList;
import java.util.List;

public class HologramTop {

    public static List<ArmorStand> create(){
        Location location = new Location(Bukkit.getWorld("world"),100,97,150);

        List<ArmorStand> stands = new ArrayList<>();

        stands.add(createHologram(location,"10"));
        stands.add(createHologram(location.add(0,0.3,0),"9"));
        stands.add(createHologram(location.add(0,0.3,0),"8"));
        stands.add(createHologram(location.add(0,0.3,0),"7"));
        stands.add(createHologram(location.add(0,0.3,0),"6"));
        stands.add(createHologram(location.add(0,0.3,0),"5"));
        stands.add(createHologram(location.add(0,0.3,0),"4"));
        stands.add(createHologram(location.add(0,0.3,0),"3"));
        stands.add(createHologram(location.add(0,0.3,0),"2"));
        stands.add(createHologram(location.add(0,0.3,0),"1"));
        stands.add(createHologram(location.add(0,0.3,0),"Топ"));

        return stands;
    }

    public static void delete(){
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
