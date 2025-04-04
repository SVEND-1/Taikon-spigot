package svend.taikon.Listener;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.Model.Resource;
import svend.taikon.Taikon;

import java.util.*;

public class BreakBlockListener implements Listener {

    public static Set<Material> allowedBlocks = Collections.synchronizedSet(new HashSet<>());
    private final ConnectToMongoDB database;
    private final ResourceDB resourceDB;
    private final Map<Location, Player> brokenBlocks = new HashMap<>(); // Трекинг "сломанных" блоков

    public BreakBlockListener() {
        this.database = new ConnectToMongoDB();
        this.resourceDB = new ResourceDB(database.getDatabase());

        allowedBlocks.add(Material.WHITE_TULIP);
        allowedBlocks.add(Material.OAK_LOG);
        allowedBlocks.add(Material.STONE);
        allowedBlocks.add(Material.SAND);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakBlock(BlockBreakEvent e) {
        Material breakBlockType = e.getBlock().getType();

        if (allowedBlocks.contains(breakBlockType)) {
            updateBlock(e);
            addResourceToDataBase(e);
        } else {
            e.setCancelled(true);
        }
    }
    private void updateBlock(BlockBreakEvent e) {
        Block breakBlock = e.getBlock();
        if (breakBlock == null) {
            return;
        }
        Material breakBlockType = e.getBlock().getType();
        Location breakLocation = breakBlock.getLocation();
        e.setDropItems(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                breakLocation.getBlock().setType(breakBlockType);
            }
        }.runTaskLater(Taikon.getPlugin(), 20 * 3);
    }

    private int getMultiplier(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand == null) return 0;

        return switch (itemInHand.getType()) {
            case WOODEN_AXE, WOODEN_PICKAXE, WOODEN_SHOVEL ->  1;
            case STONE_AXE, STONE_PICKAXE, STONE_SHOVEL -> 2;
            case IRON_AXE, IRON_PICKAXE, IRON_SHOVEL -> 3;
            case DIAMOND_AXE, DIAMOND_PICKAXE, DIAMOND_SHOVEL -> 4;
            default -> 0;
        };
    }

    private void addResourceToDataBase(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Material breakBlockType = e.getBlock().getType();
        int multiplier = getMultiplier(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                Resource resource = resourceDB.read(player.getUniqueId());

                switch (breakBlockType) {
                    case WHITE_TULIP:
                        resource.setFlowers(resource.getFlowers() + 1);
                        break;
                    case OAK_LOG:
                        if (isAxe(player)) resource.setWood(resource.getWood() + multiplier);
                        break;
                    case STONE:
                        if (isPickaxe(player)) resource.setStone(resource.getStone() + multiplier);
                        break;
                    case SAND:
                        if (isShovel(player)) resource.setSand(resource.getSand() + multiplier);
                        break;
                }

                resourceDB.update(resource);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    private boolean isAxe(Player player) {
        Material tool = player.getInventory().getItemInMainHand().getType();
        return tool == Material.WOODEN_AXE || tool == Material.STONE_AXE
                || tool == Material.IRON_AXE || tool == Material.DIAMOND_AXE;
    }

    private boolean isPickaxe(Player player) {
        Material tool = player.getInventory().getItemInMainHand().getType();
        return tool == Material.WOODEN_PICKAXE || tool == Material.STONE_PICKAXE
                || tool == Material.IRON_PICKAXE || tool == Material.DIAMOND_PICKAXE;
    }

    private boolean isShovel(Player player) {
        Material tool = player.getInventory().getItemInMainHand().getType();
        return tool == Material.WOODEN_SHOVEL || tool == Material.STONE_SHOVEL
                || tool == Material.IRON_SHOVEL || tool == Material.DIAMOND_SHOVEL;
    }
}
