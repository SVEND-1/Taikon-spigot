package svend.taikon.Task;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.Taikon;
import svend.taikon.View.HologramTop;

import java.util.ArrayList;
import java.util.List;

public class UpdateHologramTask extends BukkitRunnable {
    private final HologramTop hologramTop;
    private List<org.bukkit.entity.ArmorStand> holograms;

    public UpdateHologramTask(HologramTop hologramTop) {
        this.hologramTop = hologramTop;
        this.holograms = hologramTop.create();
    }

    @Override
    public void run() {
        Bukkit.getScheduler().runTask(Taikon.getPlugin(), () -> {
            for (ArmorStand stand : holograms) {
                if (stand != null) {
                    stand.remove();
                }
            }

            List<org.bukkit.entity.ArmorStand> newHolograms = hologramTop.create();
            if (newHolograms == null) {
                this.holograms = new ArrayList<>();
            } else {
                this.holograms = newHolograms;
            }
        });
    }
}
