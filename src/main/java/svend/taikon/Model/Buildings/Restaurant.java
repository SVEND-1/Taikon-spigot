package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Model.Product;

import java.util.UUID;

public class Restaurant extends Building{

    public Restaurant() {
    }

    public Restaurant(String name, LargeNumber price, LargeNumber upIncome, int level, Product firstProduct, Product secondProduct, boolean buildingsConstructed, UUID userId) {
        super(name, price, upIncome, level, firstProduct, secondProduct, buildingsConstructed, userId);
    }
}
