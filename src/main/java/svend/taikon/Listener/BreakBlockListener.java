package svend.taikon.Listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.ModelDAO.ResourceDB;
import svend.taikon.Model.Resource;
import svend.taikon.Taikon;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BreakBlockListener implements Listener {

    public static Set<Material> allowedBlocks = Collections.synchronizedSet(new HashSet<>());
    private final ConnectToMongoDB database;
    private final ResourceDB resourceDB;

    public BreakBlockListener() {
        this.database = new ConnectToMongoDB();
        this.resourceDB = new ResourceDB(database.getDatabase());

        allowedBlocks.add(Material.WHITE_TULIP);
        allowedBlocks.add(Material.OAK_LOG);
        allowedBlocks.add(Material.STONE);
        allowedBlocks.add(Material.SAND);
    }

    @EventHandler
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
        if (itemInHand == null) {
            return 0;
        }
        Material itemInMainHand = itemInHand.getType();
        int multiplier = 0;
        if (itemInMainHand == Material.WOODEN_AXE ||
                itemInMainHand == Material.WOODEN_PICKAXE ||
                itemInMainHand == Material.WOODEN_SHOVEL) {
            multiplier = 1;
        } else if (itemInMainHand == Material.STONE_AXE ||
                itemInMainHand == Material.STONE_PICKAXE ||
                itemInMainHand == Material.STONE_SHOVEL) {
            multiplier = 2;
        } else if (itemInMainHand == Material.IRON_AXE ||
                itemInMainHand == Material.IRON_PICKAXE ||
                itemInMainHand == Material.IRON_SHOVEL) {
            multiplier = 3;
        } else if (itemInMainHand == Material.DIAMOND_AXE ||
                itemInMainHand == Material.DIAMOND_PICKAXE ||
                itemInMainHand == Material.DIAMOND_SHOVEL) {
            multiplier = 4;
        }
        return multiplier;
    }

    private void addResourceToDataBase(BlockBreakEvent e) {
        Player player = e.getPlayer();

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Material breakBlockType = e.getBlock().getType();
        Material itemInMainHand = itemInHand.getType();
        int multiplier = getMultiplier(player);

        if (itemInHand == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Resource resource = resourceDB.read(player.getUniqueId());

                if (breakBlockType == Material.WHITE_TULIP) {
                    resource.setFlowers(resource.getFlowers() + 1);
                    resourceDB.update(resource);
                    return;
                }

                if (itemInMainHand == Material.WOODEN_AXE ||
                        itemInMainHand == Material.STONE_AXE ||
                        itemInMainHand == Material.IRON_AXE ||
                        itemInMainHand == Material.DIAMOND_AXE) {
                    if (breakBlockType == Material.OAK_LOG) {
                        resource.setWood(resource.getWood() + multiplier);
                    }
                } else if (itemInMainHand == Material.WOODEN_PICKAXE ||
                        itemInMainHand == Material.STONE_PICKAXE ||
                        itemInMainHand == Material.IRON_PICKAXE ||
                        itemInMainHand == Material.DIAMOND_PICKAXE) {
                    if (breakBlockType == Material.STONE) {
                        resource.setStone(resource.getStone() + multiplier);
                    }
                } else if (itemInMainHand == Material.WOODEN_SHOVEL ||
                        itemInMainHand == Material.STONE_SHOVEL ||
                        itemInMainHand == Material.IRON_SHOVEL ||
                        itemInMainHand == Material.DIAMOND_SHOVEL) {
                    if (breakBlockType == Material.SAND) {
                        resource.setSand(resource.getSand() + multiplier);
                    }
                } else {
                    player.sendMessage("Вам нужно купить инструменты чтобы получать ресурсы");
                }

                resourceDB.update(resource);
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }
}
