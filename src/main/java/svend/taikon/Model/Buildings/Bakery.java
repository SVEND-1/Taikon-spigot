package svend.taikon.Model.Buildings;

import java.util.UUID;

public class Bakery extends Building {
    public Bakery() {
    }

    public Bakery(String name, int price, int upIncome, int level, UUID userId) {
        super(name, price, upIncome, level, userId);
    }
}
