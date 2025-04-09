package svend.taikon.Model;

import svend.taikon.LargeNumber;

public class Product {
    private String name;
    private LargeNumber price;
    private int count;
    private int lvl;
    private int time;
    private boolean open;

    public Product() {
    }

    public Product(String name, LargeNumber price,int count ,int lvl,int time,boolean open) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.lvl = lvl;
        this.time = time;
        this.open = open;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", lvl=" + lvl +
                ", open=" + open +
                '}';
    }
}
