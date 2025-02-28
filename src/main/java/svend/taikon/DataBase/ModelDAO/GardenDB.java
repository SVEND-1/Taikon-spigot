package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class GardenDB implements DAO<Garden, UUID> {
    private final MongoCollection<Document> gardenCollection;

    public GardenDB(MongoDatabase database) {
        this.gardenCollection = database.getCollection("Garden");
    }

    @Override
    public boolean insert(Garden garden) {
        gardenCollection.insertOne(createBakeryDocument(garden));
        return true;
    }

    @Override
    public Garden read(UUID userId) {
        Document doc = gardenCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToBakery(doc) : null;
    }

    @Override
    public boolean update(Garden garden) {
        UpdateResult result = gardenCollection.replaceOne(eq("userId", garden.getUserId().toString()), createBakeryDocument(garden));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Garden garden) {
        DeleteResult result = gardenCollection.deleteOne(eq("userId", garden.getUserId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Garden> getAll() {
        return StreamSupport.stream(gardenCollection.find().spliterator(), false)
                .map(this::mapDocumentToBakery)
                .collect(Collectors.toList());
    }

    private Document createBakeryDocument(Garden garden) {
        return new Document("name", garden.getName())
                .append("price", garden.getPrice())
                .append("level", garden.getLevel())
                .append("upIncome", garden.getUpIncome())
                .append("userId", garden.getUserId().toString());
    }

    private Garden mapDocumentToBakery(Document doc) {
        Garden garden = new Garden();
        garden.setName(doc.getString("name"));
        garden.setPrice(doc.getInteger("price"));
        garden.setUpIncome(doc.getInteger("upIncome"));
        garden.setLevel(doc.getInteger("level"));
        garden.setUserId(UUID.fromString(doc.getString("userId")));
        return garden;
    }
}
