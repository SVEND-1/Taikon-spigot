package svend.taikon.Model.Buildings;

import java.util.UUID;

public class Restaurant extends Building{

    public Restaurant() {
    }

    public Restaurant(String name, int price, int upIncome, int level, UUID userId) {
        super(name, price, upIncome, level, userId);
    }
}
