package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Restaurant;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class RestaurantDB implements DAO<Restaurant, UUID> {
    private final MongoCollection<Document> restaurantCollection;

    public RestaurantDB(MongoDatabase database) {
        this.restaurantCollection = database.getCollection("Restaurant");
    }

    @Override
    public boolean insert(Restaurant restaurant) {
        restaurantCollection.insertOne(createBakeryDocument(restaurant));
        return true;
    }

    @Override
    public Restaurant read(UUID userId) {
        Document doc = restaurantCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToBakery(doc) : null;
    }

    @Override
    public boolean update(Restaurant restaurant) {
        UpdateResult result = restaurantCollection.replaceOne(eq("userId", restaurant.getUserId().toString()), createBakeryDocument(restaurant));
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
                .map(this::mapDocumentToBakery)
                .collect(Collectors.toList());
    }

    private Document createBakeryDocument(Restaurant restaurant) {
        return new Document("name", restaurant.getName())
                .append("price", restaurant.getPrice())
                .append("level", restaurant.getLevel())
                .append("upIncome", restaurant.getUpIncome())
                .append("userId", restaurant.getUserId().toString());
    }

    private Restaurant mapDocumentToBakery(Document doc) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(doc.getString("name"));
        restaurant.setPrice(doc.getInteger("price"));
        restaurant.setUpIncome(doc.getInteger("upIncome"));
        restaurant.setLevel(doc.getInteger("level"));
        restaurant.setUserId(UUID.fromString(doc.getString("userId")));
        return restaurant;
    }
}