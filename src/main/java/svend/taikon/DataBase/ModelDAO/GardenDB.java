package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import svend.taikon.DataBase.DAO;
import svend.taikon.DataBase.DataBaseManager;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class GardenDB implements DAO<Garden, UUID> {
    private final MongoCollection<Document> gardenCollection;
    private final ProductDB productDB;

    public GardenDB(MongoDatabase database) {
        this.gardenCollection = database.getCollection("Garden");
        this.productDB = new ProductDB(database);
    }

    @Override
    public boolean insert(Garden garden) {
        gardenCollection.insertOne(createGardenDocument(garden));
        return true;
    }

    @Override
    public Garden read(UUID userId) {
        Document doc = gardenCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToGarden(doc) : null;
    }

    @Override
    public boolean update(Garden garden) {
        UpdateResult result = gardenCollection.replaceOne(
                eq("userId", garden.getUserId().toString()),
                createGardenDocument(garden)
        );
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
                .map(this::mapDocumentToGarden)
                .collect(Collectors.toList());
    }

    private Document createGardenDocument(Garden garden) {
        return new Document()
                .append("name", garden.getName())
                .append("price", garden.getPrice().getValue().toString())
                .append("upIncome", garden.getUpIncome().getValue().toString())
                .append("level", garden.getLevel())
                .append("firstProduct", garden.getFirstProduct() != null ?
                        productDB.createProductDocument(garden.getFirstProduct()) : null)
                .append("secondProduct", garden.getSecondProduct() != null ?
                        productDB.createProductDocument(garden.getSecondProduct()) : null)
                .append("buildingsConstructed",garden.isBuildingsConstructed())
                .append("userId", garden.getUserId().toString());
    }

    private Garden mapDocumentToGarden(Document doc) {
        Document firstProductDoc = doc.get("firstProduct", Document.class);
        Document secondProductDoc = doc.get("secondProduct", Document.class);

        return new Garden(
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