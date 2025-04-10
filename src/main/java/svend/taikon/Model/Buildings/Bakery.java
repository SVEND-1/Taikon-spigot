package svend.taikon.Model.Buildings;

import svend.taikon.LargeNumber;
import svend.taikon.Model.Product;

import java.util.UUID;

public class Bakery extends Building {
    public Bakery() {
    }
    //Todo:добавить производства
    //25 первый продукт 50 улучшение первого продукта 75 второй 100 улучшение второго


    public Bakery(String name, LargeNumber price, LargeNumber upIncome, int level, Product firstProduct, Product secondProduct, boolean buildingsConstructed, UUID userId) {
        super(name, price, upIncome, level, firstProduct, secondProduct, buildingsConstructed, userId);
    }
}
