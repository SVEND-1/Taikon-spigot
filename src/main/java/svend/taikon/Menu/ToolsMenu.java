package svend.taikon.Menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Utility.AdminUtils;
import svend.taikon.Utility.MenuUtils;

public class ToolsMenu extends MenuManager {

    private final UserDB userDB;
    private final DataBaseManager dataBaseManager;

    private ItemStack woodenAxe, stoneAxe, ironAxe;
    private ItemStack woodenPickaxe, stonePickaxe, ironPickaxe;
    private ItemStack woodenShovel, stoneShovel, ironShovel;

    public ToolsMenu(Player player) {
        super(player);
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        initializeItems();
    }

    @Override
    public String getMenuName() {
        return "§6§lМагазин инструментов";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    private void initializeItems() {
        woodenAxe = MenuUtils.createMenuItemWithLore(
                Material.WOODEN_AXE,
                "§6✦ §eДеревянный топор §6✦",
                "§8→ §7Цена: §a10 монет",
                "§8→ §7Профит: §2+1"
        );

        stoneAxe = MenuUtils.createMenuItemWithLore(
                Material.STONE_AXE,
                "§f✦ §7Каменный топор §f✦",
                "§8→ §7Цена: §a30 монет",
                "§8→ §7Профит: §2+2"
        );

        ironAxe = MenuUtils.createMenuItemWithLore(
                Material.IRON_AXE,
                "§7✦ §fЖелезный топор §7✦",
                "§8→ §7Цена: §a70 монет",
                "§8→ §7Профит: §2+3"
        );

        woodenPickaxe = MenuUtils.createMenuItemWithLore(
                Material.WOODEN_PICKAXE,
                "§6✦ §eДеревянная кирка §6✦",
                "§8→ §7Цена: §a10 монет",
                "§8→ §7Профит: §e+1"
        );

        stonePickaxe = MenuUtils.createMenuItemWithLore(
                Material.STONE_PICKAXE,
                "§f✦ §7Каменная кирка §f✦",
                "§8→ §7Цена: §a30 монет",
                "§8→ §7Профит: §e+2"
        );

        ironPickaxe = MenuUtils.createMenuItemWithLore(
                Material.IRON_PICKAXE,
                "§7✦ §fЖелезная кирка §7✦",
                "§8→ §7Цена: §a70 монет",
                "§8→ §7Профит: §e+3"
        );

        woodenShovel = MenuUtils.createMenuItemWithLore(
                Material.WOODEN_SHOVEL,
                "§6✦ §eДеревянная лопата §6✦",
                "§8→ §7Цена: §a10 монет",
                "§8→ §7Профит: §b+1"
        );

        stoneShovel = MenuUtils.createMenuItemWithLore(
                Material.STONE_SHOVEL,
                "§f✦ §7Каменная лопата §f✦",
                "§8→ §7Цена: §a30 монет",
                "§8→ §7Профит: §b+2"
        );

        ironShovel = MenuUtils.createMenuItemWithLore(
                Material.IRON_SHOVEL,
                "§7✦ §fЖелезная лопата §7✦",
                "§8→ §7Цена: §a70 монет",
                "§8→ §7Профит: §b+3"
        );
    }

    private void purchase(int price, ItemStack itemToGive) {
        new BukkitRunnable() {
            @Override
            public void run() {
                User user = userDB.read(player.getUniqueId());
                LargeNumber itemPrice = new LargeNumber(String.valueOf(price));

                if (user.getBalance().leftOrEqual(itemPrice)) {
                    user.setBalance(user.getBalance().subtract(itemPrice));
                    userDB.update(user);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack itemCopy = new ItemStack(itemToGive);
                            player.getInventory().addItem(itemCopy);
                            player.sendMessage("§aВы успешно приобрели " + itemToGive.getItemMeta().getDisplayName());
                        }
                    }.runTask(Taikon.getPlugin());
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage("§cНедостаточно средств для покупки.");
                        }
                    }.runTask(Taikon.getPlugin());
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        if (!e.getView().getTitle().equals(getMenuName())) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        boolean hasItem = false;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == clickedItem.getType()) {
                hasItem = true;
                break;
            }
        }

        if (hasItem) {
            player.sendMessage("§cУ вас уже есть этот инструмент!");
            return;
        }

        switch (clickedItem.getType()) {
            case WOODEN_AXE:
                purchase(10, woodenAxe);
                break;
            case STONE_AXE:
                purchase(30, stoneAxe);
                break;
            case IRON_AXE:
                purchase(70, ironAxe);
                break;
            case WOODEN_PICKAXE:
                purchase(10, woodenPickaxe);
                break;
            case STONE_PICKAXE:
                purchase(30, stonePickaxe);
                break;
            case IRON_PICKAXE:
                purchase(70, ironPickaxe);
                break;
            case WOODEN_SHOVEL:
                purchase(10, woodenShovel);
                break;
            case STONE_SHOVEL:
                purchase(30, stoneShovel);
                break;
            case IRON_SHOVEL:
                purchase(70, ironShovel);
                break;
        }
    }

    @Override
    public void setMenuItems() {
        inventory.clear();

        inventory.setItem(11, woodenAxe);
        inventory.setItem(13, woodenPickaxe);
        inventory.setItem(15, woodenShovel);

        inventory.setItem(20, stoneAxe);
        inventory.setItem(22, stonePickaxe);
        inventory.setItem(24, stoneShovel);

        inventory.setItem(29, ironAxe);
        inventory.setItem(31, ironPickaxe);
        inventory.setItem(33, ironShovel);
    }
}