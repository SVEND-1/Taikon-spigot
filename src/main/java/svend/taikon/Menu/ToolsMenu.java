package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolsMenu extends MenuManager{

    public ToolsMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return "Магазин инструментов";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e)  {
        if (e.getView().getTitle().equals(getMenuName())) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            Player player = (Player) e.getView().getPlayer();

            ItemStack itemClick = e.getCurrentItem();

            int price = Integer.parseInt(itemClick.getItemMeta().getDisplayName());
            int balance = 0;//Типо баланс игрока

            if(balance >= price){
                player.getInventory().addItem(itemClick);
                balance -= price;
            }

            player.getInventory().addItem(itemClick);
            e.setCancelled(true);
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack woodenAxe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta woodenAxeMeta = woodenAxe.getItemMeta();
        woodenAxeMeta.setDisplayName("10");
        woodenAxe.setItemMeta(woodenAxeMeta);

        ItemStack stoneAxe = new ItemStack(Material.STONE_AXE);
        ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
        stoneAxeMeta.setDisplayName("30");
        stoneAxe.setItemMeta(stoneAxeMeta);

        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
        ItemMeta ironAxeMeta = ironAxe.getItemMeta();
        ironAxeMeta.setDisplayName("70");
        ironAxe.setItemMeta(ironAxeMeta);

        ItemStack woodenPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta woodenPickaxeMeta = woodenPickaxe.getItemMeta();
        woodenPickaxeMeta.setDisplayName("10");
        woodenPickaxe.setItemMeta(woodenPickaxeMeta);

        ItemStack stonePickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta stonePickaxeMeta = stonePickaxe.getItemMeta();
        stonePickaxeMeta.setDisplayName("30");
        stonePickaxe.setItemMeta(stonePickaxeMeta);

        ItemStack ironPickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta ironPickaxeMeta = ironPickaxe.getItemMeta();
        ironPickaxeMeta.setDisplayName("70");
        ironPickaxe.setItemMeta(ironPickaxeMeta);

        ItemStack woodenShovel = new ItemStack(Material.WOODEN_SHOVEL);
        ItemMeta woodenShovelMeta = woodenShovel.getItemMeta();
        woodenShovelMeta.setDisplayName("10");
        woodenShovel.setItemMeta(woodenShovelMeta);

        ItemStack stoneShovel = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta stoneShovelMeta = stoneShovel.getItemMeta();
        stoneShovelMeta.setDisplayName("30");
        stoneShovel.setItemMeta(stoneShovelMeta);

        ItemStack ironShovel  = new ItemStack(Material.IRON_SHOVEL);
        ItemMeta ironShovelMeta = ironShovel.getItemMeta();
        ironShovelMeta.setDisplayName("70");
        ironShovel.setItemMeta(ironShovelMeta);

        inventory.setItem(11,woodenAxe);
        inventory.setItem(13,woodenPickaxe);
        inventory.setItem(15,woodenShovel);

        inventory.setItem(20,stoneAxe);
        inventory.setItem(22,stonePickaxe);
        inventory.setItem(24,stoneShovel);

        inventory.setItem(29,ironAxe);
        inventory.setItem(31,ironPickaxe);
        inventory.setItem(33,ironShovel);
    }
}
