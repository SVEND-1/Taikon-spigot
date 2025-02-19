package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Resource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class ResourceDB implements DAO<Resource, UUID> {
    private final MongoCollection<Document> resourceCollection;

    public ResourceDB(MongoDatabase database) {
        this.resourceCollection = database.getCollection("Resources");
    }

    @Override
    public boolean insert(Resource resource) {
        resourceCollection.insertOne(createResourceDocument(resource));
        return true;
    }

    @Override
    public Resource read(UUID userId) {
        Document doc = resourceCollection.find(eq("userId", userId.toString())).first();
        return doc != null ? mapDocumentToResource(doc) : null;
    }

    @Override
    public boolean update(Resource resource) {
        UpdateResult result = resourceCollection.replaceOne(eq("userId", resource.getUserId().toString()), createResourceDocument(resource));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(Resource resource) {
        DeleteResult result = resourceCollection.deleteOne(eq("userId", resource.getUserId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<Resource> getAll() {
        return StreamSupport.stream(resourceCollection.find().spliterator(), false)
                .map(this::mapDocumentToResource)
                .collect(Collectors.toList());
    }

    private Document createResourceDocument(Resource resource) {
        return new Document("flower", resource.getFlowers())
                .append("wood", resource.getWood())
                .append("stone", resource.getStone())
                .append("sand", resource.getSand())
                .append("userId", resource.getUserId().toString());
    }

    private Resource mapDocumentToResource(Document doc) {
        Resource resource = new Resource();
        resource.setFlowers(doc.getInteger("flower"));
        resource.setWood(doc.getInteger("wood"));
        resource.setStone(doc.getInteger("stone"));
        resource.setSand(doc.getInteger("sand"));
        resource.setUserId(UUID.fromString(doc.getString("userId")));
        return resource;
    }
}