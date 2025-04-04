package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Building;

import java.util.UUID;

public class Garden extends Building {
    public Garden() {
    }

    public Garden(String name, LargeNumber price, LargeNumber upIncome, int level, UUID userId) {
        super(name, price, upIncome, level, userId);
    }
}
