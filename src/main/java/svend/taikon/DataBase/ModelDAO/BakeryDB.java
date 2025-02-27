package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Bakery;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class BakeryDB implements DAO<Bakery, UUID> {
    private final MongoCollection<Document> bakeryCollection;

    public BakeryDB(MongoDatabase database) {
        this.bakeryCollection = database.getCollection("Bakery");
    }

    @Override
    public boolean insert(Bakery bakery) {
        bakeryCollection.insertOne(createBakeryDocument(bakery));
        return true;
    }

    @Override
    public Bakery read(UUID userId) {
        Document doc = bakeryCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToBakery(doc) : null;
    }

    @Override
    public boolean update(Bakery bakery) {
        UpdateResult result = bakeryCollection.replaceOne(eq("userId", bakery.getUserId().toString()), createBakeryDocument(bakery));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Bakery bakery) {
        DeleteResult result = bakeryCollection.deleteOne(eq("userId", bakery.getUserId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Bakery> getAll() {
        return StreamSupport.stream(bakeryCollection.find().spliterator(), false)
                .map(this::mapDocumentToBakery)
                .collect(Collectors.toList());
    }

    private Document createBakeryDocument(Bakery bakery) {
        return new Document("name", bakery.getName())
                .append("price", bakery.getPrice())
                .append("level", bakery.getLevel())
                .append("upIncome", bakery.getUpIncome())
                .append("userId", bakery.getUserId().toString());
    }

    private Bakery mapDocumentToBakery(Document doc) {
        Bakery bakery = new Bakery();
        bakery.setName(doc.getString("name"));
        bakery.setPrice(doc.getInteger("price"));
        bakery.setUpIncome(doc.getInteger("upIncome"));
        bakery.setLevel(doc.getInteger("level"));
        bakery.setUserId(UUID.fromString(doc.getString("userId")));
        return bakery;
    }
}