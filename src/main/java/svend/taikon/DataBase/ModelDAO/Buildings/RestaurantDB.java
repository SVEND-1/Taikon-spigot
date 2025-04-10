package svend.taikon.DataBase.ModelDAO.Buildings;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class RestaurantDB implements DAO<Restaurant, UUID> {
    private final MongoCollection<Document> restaurantCollection;
    private final ProductDB productDB;

    public RestaurantDB(MongoDatabase database) {
        this.restaurantCollection = database.getCollection("Restaurant");
        this.productDB = new ProductDB(database);
    }

    @Override
    public boolean insert(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }

        try {
            restaurantCollection.insertOne(createRestaurantDocument(restaurant));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Restaurant read(UUID userId) {
        try {
            Document doc = restaurantCollection.find(eq("userId", userId.toString())).first();
            return doc != null ? mapDocumentToRestaurant(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean update(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }

        try {
            UpdateResult result = restaurantCollection.replaceOne(
                    eq("userId", restaurant.getUserId().toString()),
                    createRestaurantDocument(restaurant)
            );
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Restaurant restaurant) {
        if (restaurant == null) {
            return false;
        }

        try {
            DeleteResult result = restaurantCollection.deleteOne(eq("userId", restaurant.getUserId().toString()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Restaurant> getAll() {
        try {
            return StreamSupport.stream(restaurantCollection.find().spliterator(), false)
                    .map(this::mapDocumentToRestaurant)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Document createRestaurantDocument(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        try {
            return new Document("name", restaurant.getName())
                    .append("price", restaurant.getPrice().getValue().toString())
                    .append("upIncome", restaurant.getUpIncome().getValue().toString())
                    .append("level", restaurant.getLevel())
                    .append("firstProduct", restaurant.getFirstProduct() != null ?
                            productDB.createProductDocument(restaurant.getFirstProduct()) : null)
                    .append("secondProduct", restaurant.getSecondProduct() != null ?
                            productDB.createProductDocument(restaurant.getSecondProduct()) : null)
                    .append("buildingsConstructed", restaurant.isBuildingsConstructed())
                    .append("userId", restaurant.getUserId().toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Restaurant mapDocumentToRestaurant(Document doc) {
        if (doc == null) {
            return null;
        }

        try {
            Document firstProductDoc = doc.get("firstProduct", Document.class);
            Document secondProductDoc = doc.get("secondProduct", Document.class);

            return new Restaurant(
                    doc.getString("name"),
                    new LargeNumber(doc.getString("price")),
                    new LargeNumber(doc.getString("upIncome")),
                    doc.getInteger("level"),
                    firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                    secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                    doc.getBoolean("buildingsConstructed"),
                    UUID.fromString(doc.getString("userId"))
            );
        } catch (Exception e) {
            return null;
        }
    }
}
