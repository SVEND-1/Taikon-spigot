package svend.taikon.Menu.BuildingsMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;
import svend.taikon.DataBase.ConnectToMongoDB;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.DataBase.ModelDAO.BakeryDB;
import svend.taikon.DataBase.ModelDAO.UserDB;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.MenuManager;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Product;
import svend.taikon.Model.User;
import svend.taikon.Taikon;
import svend.taikon.Task.ProductTask;
import svend.taikon.Task.ProductTaskManager;
import svend.taikon.Utility.MenuUtils;

public class BakeryMenu extends MenuManager {
    private final UserDB userDB;
    private final BakeryDB bakeryDB;
    private Bakery bakery;
    private final DataBaseManager dataBaseManager;
    public BakeryMenu(Player player) {
        super(player);
        this.dataBaseManager = DataBaseManager.getInstance();
        this.userDB = dataBaseManager.getUserDB();
        this.bakeryDB = dataBaseManager.getBakeryDB();
    }

    @Override
    public String getMenuName() {
        return "Пекарня";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        e.setCancelled(true);

        if (!e.getView().getTitle().equals(getMenuName())) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        Material clickedItemType = e.getCurrentItem() != null ? e.getCurrentItem().getType() : null;

        if (clickedItemType == null) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    User user = userDB.read(player.getUniqueId());
                    Bakery bakery = bakeryDB.read(player.getUniqueId());

                    if (user == null || bakery == null) {
                        player.sendMessage("Ошибка данных");
                        return;
                    }

                    Product firstProduct = bakery.getFirstProduct();
                    Product secondProduct = bakery.getSecondProduct();

                    switch (clickedItemType) {
                        case GREEN_STAINED_GLASS:
                            MenuUtils.handleBuildingUpgrade(player, bakery, bakeryDB, user, userDB, new LargeNumber("10"));
                            break;

                        case OBSIDIAN:
                            handleProductUpgrade(player, firstProduct, bakery, bakeryDB);
                            break;

                        case FEATHER:
                            handleProductSell(player, firstProduct, user, bakery, userDB, bakeryDB);
                            break;

                        case PAPER:
                            handleProductUpgrade(player, secondProduct, bakery, bakeryDB);
                            break;

                        case ARROW:
                            handleProductSell(player, secondProduct, user, bakery, userDB, bakeryDB);
                            break;

                        case RED_STAINED_GLASS:
                            player.closeInventory();
                            break;
                    }
                } catch (Exception e) {
                    player.sendMessage("Произошла ошибка");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Taikon.getPlugin());
    }

    @Override
    public void setMenuItems() {
        MenuUtils.ItemProfitableBuildings(inventory,bakery);
    }

    private void handleProductUpgrade(Player player, Product product, Bakery bakery, BakeryDB bakeryDB) {
        if (product.getLvl() >= 2) {
            player.sendMessage("У вас максимальная прокачка");
            return;
        }

        product.setOpen(true);
        product.setLvl(2);
        bakeryDB.update(bakery);

        // Запускаем задачу только если она еще не запущена
        if (!ProductTaskManager.hasTask(player.getUniqueId())) {
            ProductTask task = new ProductTask(player.getUniqueId(), bakeryDB, userDB);
            task.runTaskTimerAsynchronously(Taikon.getPlugin(), 0, 100);
            ProductTaskManager.addTask(player.getUniqueId(), task);
        }
    }

    private void handleProductSell(Player player, Product product, User user, Bakery bakery,
                                   UserDB userDB, BakeryDB bakeryDB) {
        if (product.getCount() <= 0) {
            player.sendMessage("У вас 0 товара");
            return;
        }

        // Атомарные операции
        try {
            // 1. Уменьшаем количество
            product.setCount(product.getCount() - 1);

            // 2. Добавляем деньги
            LargeNumber price = product.getPrice();
            user.setBalance(user.getBalance().add(price));

            // 3. Сохраняем изменения
            userDB.update(user);
            bakeryDB.update(bakery);

            player.sendMessage("Продано! +" + price);
        } catch (Exception e) {
            player.sendMessage("Ошибка при продаже");
            e.printStackTrace();
        }
    }
}
