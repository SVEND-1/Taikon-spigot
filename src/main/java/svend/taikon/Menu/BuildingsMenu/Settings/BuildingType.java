package svend.taikon.Menu.BuildingsMenu.Settings;

public enum BuildingType {
    BAKERY("Пекарня", "Bakery"),
    GARDEN("Сад", "Garden"),
    RESTAURANT("Ресторан","Restaurant")
    ;

    private final String displayName;
    private final String collectionName;

    BuildingType(String displayName, String collectionName) {
        this.displayName = displayName;
        this.collectionName = collectionName;
    }
}
