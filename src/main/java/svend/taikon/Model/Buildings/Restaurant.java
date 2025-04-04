package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;

import java.util.UUID;

public class Restaurant extends Building{

    public Restaurant() {
    }

    public Restaurant(String name, LargeNumber price, LargeNumber upIncome, int level, UUID userId) {
        super(name, price, upIncome, level, userId);
    }
}
