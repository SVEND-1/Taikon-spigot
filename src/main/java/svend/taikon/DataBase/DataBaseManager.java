package svend.taikon.DataBase;

import svend.taikon.DataBase.ModelDAO.*;

public class DataBaseManager {
    private final ConnectToMongoDB database;
    private static DataBaseManager instance;
    private final UserDB userDB;
    private final BakeryDB bakeryDB;
    private final GardenDB gardenDB;
    private final RestaurantDB restaurantDB;
    private final ProductDB productDB;
    private final ResourceDB resourceDB;

    private DataBaseManager() {
        this.database = new ConnectToMongoDB();
        this.userDB = new UserDB(database.getDatabase());
        this.bakeryDB = new BakeryDB(database.getDatabase());
        this.gardenDB = new GardenDB(database.getDatabase());
        this.restaurantDB = new RestaurantDB(database.getDatabase());
        this.productDB = new ProductDB(database.getDatabase());
        this.resourceDB = new ResourceDB(database.getDatabase());
    }
    public static synchronized DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }
    public UserDB getUserDB() {
        return userDB;
    }

    public BakeryDB getBakeryDB() {
        return bakeryDB;
    }

    public GardenDB getGardenDB() {
        return gardenDB;
    }

    public RestaurantDB getRestaurantDB() {
        return restaurantDB;
    }

    public ProductDB getProductDB() {
        return productDB;
    }

    public ResourceDB getResourceDB() {
        return resourceDB;
    }
}
