package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Restaurant;

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
        restaurantCollection.insertOne(createRestaurantDocument(restaurant));
        return true;
    }

    @Override
    public Restaurant read(UUID userId) {
        Document doc = restaurantCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToRestaurant(doc) : null;
    }

    @Override
    public boolean update(Restaurant restaurant) {
        UpdateResult result = restaurantCollection.replaceOne(
                eq("userId", restaurant.getUserId().toString()),
                createRestaurantDocument(restaurant)
        );
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Restaurant restaurant) {
        DeleteResult result = restaurantCollection.deleteOne(eq("userId", restaurant.getUserId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Restaurant> getAll() {
        return StreamSupport.stream(restaurantCollection.find().spliterator(), false)
                .map(this::mapDocumentToRestaurant)
                .collect(Collectors.toList());
    }

    private Document createRestaurantDocument(Restaurant restaurant) {
        return new Document("name", restaurant.getName())
                .append("price", restaurant.getPrice().getValue().toString())
                .append("upIncome", restaurant.getUpIncome().getValue().toString())
                .append("level", restaurant.getLevel())
                .append("firstProduct", restaurant.getFirstProduct() != null ?
                        productDB.createProductDocument(restaurant.getFirstProduct()) : null)
                .append("secondProduct", restaurant.getSecondProduct() != null ?
                        productDB.createProductDocument(restaurant.getSecondProduct()) : null)
                .append("userId", restaurant.getUserId().toString());
    }

    private Restaurant mapDocumentToRestaurant(Document doc) {
        Document firstProductDoc = doc.get("firstProduct", Document.class);
        Document secondProductDoc = doc.get("secondProduct", Document.class);

        return new Restaurant(
                doc.getString("name"),
                new LargeNumber(doc.getString("price")),
                new LargeNumber(doc.getString("upIncome")),
                doc.getInteger("level"),
                firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                UUID.fromString(doc.getString("userId"))
        );
    }
}