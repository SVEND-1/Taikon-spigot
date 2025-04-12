package svend.taikon.Menu.BuildingsMenu.Settings;

import svend.taikon.LargeNumber;

public class ProductUpgrade {
    private final ResourceCost level1Cost;
    private final LargeNumber level1Price;
    private final ResourceCost level2Cost;
    private final LargeNumber level2Price;

    public ProductUpgrade(ResourceCost level1Cost, LargeNumber level1Price,
                          ResourceCost level2Cost, LargeNumber level2Price) {
        this.level1Cost = level1Cost;
        this.level1Price = level1Price;
        this.level2Cost = level2Cost;
        this.level2Price = level2Price;
    }

    // Геттеры
    public ResourceCost getLevel1Cost() {
        return level1Cost;
    }

    public LargeNumber getLevel1Price() {
        return level1Price;
    }

    public ResourceCost getLevel2Cost() {
        return level2Cost;
    }

    public LargeNumber getLevel2Price() {
        return level2Price;
    }
}
