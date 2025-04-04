package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;

import java.util.UUID;

public class Building {
    private String name;
    private LargeNumber price;
    private LargeNumber upIncome;//На сколько будет увеличивать доход
    private int level;
    private UUID userId;

    public Building() {}

    public Building(String name, LargeNumber price, LargeNumber upIncome, int level, UUID userId) {
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
