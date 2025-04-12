package svend.taikon.Menu.BuildingsMenu.Settings;

public class ResourceCost {
    private final int wood;
    private final int stone;
    private final int sand;

    public ResourceCost(int wood, int stone, int sand) {
        this.wood = wood;
        this.stone = stone;
        this.sand = sand;
    }

    // Геттеры
    public int getWood() {
        return wood;
    }

    public int getStone() {
        return stone;
    }

    public int getSand() {
        return sand;
    }
}