package svend.taikon.Model.Buildings;

import java.util.UUID;

public class Building {
    private String name;
    private int price;
    private int upIncome;//На сколько будет увеличивать доход
    private int level;
    private UUID userId;

    public Building() {}

    public Building(String name, int price, int upIncome, int level, UUID userId) {
        this.name = name;
        this.price = price;
        this.upIncome = upIncome;
        this.level = level;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUpIncome() {
        return upIncome;
    }

    public void setUpIncome(int upIncome) {
        this.upIncome = upIncome;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
