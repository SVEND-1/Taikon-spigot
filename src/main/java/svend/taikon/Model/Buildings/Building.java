package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductType;
import svend.taikon.Menu.BuildingsMenu.Settings.ProductUpgrade;
import svend.taikon.Model.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Building {
    protected Map<ProductType, ProductUpgrade> upgradeConfig = new HashMap<>();
    private String name;
    private LargeNumber price;
    private LargeNumber upIncome;
    private int level;
    private UUID userId;
    private Product firstProduct;
    private Product secondProduct;
    private boolean buildingsConstructed;

    public Building() {
        initUpgradeConfig();
    }

    public Building(String name, LargeNumber price, LargeNumber upIncome, int level,
                    Product firstProduct, Product secondProduct,
                    boolean buildingsConstructed, UUID userId) {
        this.name = name;
        this.price = price;
        this.upIncome = upIncome;
        this.level = level;
        this.firstProduct = firstProduct;
        this.secondProduct = secondProduct;
        this.buildingsConstructed = buildingsConstructed;
        this.userId = userId;
        initUpgradeConfig();
    }

    protected abstract void initUpgradeConfig();

    public Map<ProductType, ProductUpgrade> getUpgradeConfig() {
        return upgradeConfig;
    }

    public void setUpgradeConfig(Map<ProductType, ProductUpgrade> upgradeConfig) {
        this.upgradeConfig = upgradeConfig;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LargeNumber getPrice() {
        return price;
    }

    public void setPrice(LargeNumber price) {
        this.price = price;
    }

    public LargeNumber getUpIncome() {
        return upIncome;
    }

    public void setUpIncome(LargeNumber upIncome) {
        this.upIncome = upIncome;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Product getFirstProduct() {
        return firstProduct;
    }

    public void setFirstProduct(Product firstProduct) {
        this.firstProduct = firstProduct;
    }

    public Product getSecondProduct() {
        return secondProduct;
    }

    public void setSecondProduct(Product secondProduct) {
        this.secondProduct = secondProduct;
    }

    public boolean isBuildingsConstructed() {
        return buildingsConstructed;
    }

    public void setBuildingsConstructed(boolean buildingsConstructed) {
        this.buildingsConstructed = buildingsConstructed;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Building{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", upIncome=" + upIncome +
                ", level=" + level +
                ", userId=" + userId +
                ", firstProduct=" + firstProduct.toString() +
                ", secondProduct=" + secondProduct.toString() +
                '}';
    }
}
