package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;

import java.util.UUID;

public class Bakery extends Building {
    public Bakery() {
    }
    //Todo:добавить производства
    public Bakery(String name, LargeNumber price, LargeNumber upIncome, int level, UUID userId) {
        super(name, price, upIncome, level, userId);
    }
}
