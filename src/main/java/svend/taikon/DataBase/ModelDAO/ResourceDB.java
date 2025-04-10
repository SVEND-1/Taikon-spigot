package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.Resource;

import java.util.ArrayList;
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
        if (resource == null) {
            return false;
        }

        try {
            resourceCollection.insertOne(createResourceDocument(resource));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Resource read(UUID userId) {
        try {
            Document doc = resourceCollection.find(eq("userId", userId.toString())).first();
            return doc != null ? mapDocumentToResource(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean update(Resource resource) {
        if (resource == null) {
            return false;
        }

        try {
            UpdateResult result = resourceCollection.replaceOne(
                    eq("userId", resource.getUserId().toString()),
                    createResourceDocument(resource)
            );
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Resource resource) {
        if (resource == null) {
            return false;
        }

        try {
            DeleteResult result = resourceCollection.deleteOne(eq("userId", resource.getUserId().toString()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Resource> getAll() {
        try {
            return StreamSupport.stream(resourceCollection.find().spliterator(), false)
                    .map(this::mapDocumentToResource)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Document createResourceDocument(Resource resource) {
        if (resource == null) {
            return null;
        }

        try {
            return new Document("flower", resource.getFlowers())
                    .append("wood", resource.getWood())
                    .append("stone", resource.getStone())
                    .append("sand", resource.getSand())
                    .append("userId", resource.getUserId().toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Resource mapDocumentToResource(Document doc) {
        if (doc == null) {
            return null;
        }

        try {
            Resource resource = new Resource();
            resource.setFlowers(doc.getInteger("flower"));
            resource.setWood(doc.getInteger("wood"));
            resource.setStone(doc.getInteger("stone"));
            resource.setSand(doc.getInteger("sand"));
            resource.setUserId(UUID.fromString(doc.getString("userId")));
            return resource;
        } catch (Exception e) {
            return null;
        }
    }
}
