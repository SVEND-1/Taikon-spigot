package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductType;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductUpgrade;
import svend.taikon.Menu.BuildingsMenu.Settings.ResourceCost;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.Product;

import java.util.UUID;

public class Garden extends Building {
    @Override
    protected void initUpgradeConfig() {
        upgradeConfig.put(ProductType.FIRST, new ProductUpgrade(
                new ResourceCost(25, 25, 0),
                new LargeNumber("5000"),
                new ResourceCost(125, 0, 0),
                new LargeNumber("25000")
        ));

        upgradeConfig.put(ProductType.SECOND, new ProductUpgrade(
                new ResourceCost(250, 25, 0),
                new LargeNumber("555555"),
                new ResourceCost(1250, 125, 0),
                new LargeNumber("55555555")
        ));
    }

    public Garden() {
        super();
    }

    public Garden(String name, LargeNumber price, LargeNumber upIncome, int level,
                  Product firstProduct, Product secondProduct,
                  boolean buildingsConstructed, UUID userId) {
        super(name, price, upIncome, level, firstProduct, secondProduct,
                buildingsConstructed, userId);
    }
}
