package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.Product;

import java.util.UUID;

public class Garden extends Building {
    public Garden() {
    }

    public Garden(String name, LargeNumber price, LargeNumber upIncome, int level, Product firstProduct, Product secondProduct, UUID userId) {
        super(name, price, upIncome, level, firstProduct, secondProduct, userId);
    }
}
