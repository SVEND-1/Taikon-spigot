package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Building;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class BakeryDB implements DAO<Bakery, UUID> {
    private final MongoCollection<Document> bakeryCollection;
    private final ProductDB productDB;

    public BakeryDB(MongoDatabase database) {
        this.bakeryCollection = database.getCollection("Bakery");
        this.productDB = new ProductDB(database);
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
        UpdateResult result = bakeryCollection.replaceOne(
                eq("userId", bakery.getUserId().toString()),
                createBakeryDocument(bakery)
        );
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
                .append("price", bakery.getPrice().getValue().toString())
                .append("upIncome", bakery.getUpIncome().getValue().toString())
                .append("level", bakery.getLevel())
                .append("firstProduct", bakery.getFirstProduct() != null ?
                        productDB.createProductDocument(bakery.getFirstProduct()) : null)
                .append("secondProduct", bakery.getSecondProduct() != null ?
                        productDB.createProductDocument(bakery.getSecondProduct()) : null)
                .append("buildingsConstructed",bakery.isBuildingsConstructed())
                .append("userId", bakery.getUserId().toString());
    }

    private Bakery mapDocumentToBakery(Document doc) {

        Document firstProductDoc = doc.get("firstProduct", Document.class);
        Document secondProductDoc = doc.get("secondProduct", Document.class);

        return new Bakery(
                doc.getString("name"),
                new LargeNumber(doc.getString("price")),
                new LargeNumber(doc.getString("upIncome")),
                doc.getInteger("level"),
                firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                doc.getBoolean("buildingsConstructed"),
                UUID.fromString(doc.getString("userId"))
        );
    }
}