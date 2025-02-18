package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Resource;
import svend.taikon.Model.User;

import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class ResourceDB implements DAO<Resource, UUID> {
    private final MongoCollection<Document> resourceCollection;

    public ResourceDB(MongoDatabase database) {
        this.resourceCollection = database.getCollection("Resources");
    }

    @Override
    public boolean insert(Resource model) {
        Document doc = new Document("flower", model.getFlowers())
                .append("wood", model.getWood())
                .append("stone", model.getStone())
                .append("sand", model.getSand())
                .append("userId", model.getUserId().toString());

        resourceCollection.insertOne(doc);
        return true;
    }

    @Override
    public Resource read(UUID userId) {
        Document doc = resourceCollection.find(eq("userId", userId.toString())).first();
        if (doc != null) {
            return mapDocumentToUser(doc);
        }
        return null;
    }
    @Override
    public boolean update(Resource model) {
        Document doc = new Document("flower", model.getFlowers())
                .append("wood", model.getWood())
                .append("stone", model.getStone())
                .append("sand", model.getSand())
                .append("userId", model.getUserId().toString());

        // Обновляем документ
        UpdateResult result = resourceCollection.replaceOne(eq("userId", model.getUserId().toString()), doc);
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Resource model) {
        DeleteResult result = resourceCollection.deleteOne(eq("userId", model.getUserId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Resource mapDocumentToUser(Document doc) {
        Resource resource = new Resource();
        resource.setFlowers(doc.getInteger("flower"));
        resource.setWood(doc.getInteger("wood"));
        resource.setStone(doc.getInteger("stone"));
        resource.setSand(doc.getInteger("sand"));
        resource.setUserId(UUID.fromString(doc.getString("userId")));

        return resource;
    }
}
