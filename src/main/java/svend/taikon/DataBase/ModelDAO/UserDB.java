package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.descending;

public class UserDB implements DAO<User, UUID> {
    private final MongoCollection<Document> userCollection;

    public UserDB(MongoDatabase database) {
        this.userCollection = database.getCollection("Players");
    }

    @Override
    public boolean insert(User user) {
        if (user == null) {
            return false;
        }

        try {
            userCollection.insertOne(createUserDocument(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User read(UUID id) {
        try {
            Document doc = userCollection.find(eq("_id", id.toString())).first();
            return doc != null ? mapDocumentToUser (doc) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean update(User user) {
        if (user == null) {
            return false;
        }

        try {
            UpdateResult result = userCollection.replaceOne(
                    eq("_id", user.getId().toString()),
                    createUserDocument(user)
            );
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        if (user == null) {
            return false;
        }

        try {
            DeleteResult result = userCollection.deleteOne(eq("_id", user.getId().toString()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<User> getAll() {
        try {
            return StreamSupport.stream(userCollection.find().spliterator(), false)
                    .map(this::mapDocumentToUser )
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private Document createUserDocument(User user) {
        if (user == null) {
            return null;
        }

        try {
            return new Document("_id", user.getId().toString())
                    .append("name", user.getName())
                    .append("income", user.getIncome().getValue().toString())
                    .append("balance", user.getBalance().getValue().toString())
                    .append("incomeMultiplier", user.getIncomeMultiplier());
        } catch (Exception e) {
            return null;
        }
    }

    public User mapDocumentToUser (Document doc) {
        if (doc == null) {
            return null;
        }

        try {
            return new User(
                    UUID.fromString(doc.getString("_id")),
                    doc.getString("name"),
                    new LargeNumber(doc.getString("income")),
                    new LargeNumber(doc.getString("balance")),
                    doc.getInteger("incomeMultiplier")
            );
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> top10() {
        List<User> users = new ArrayList<>();
        try (MongoCursor<Document> cursor = userCollection.find().sort(descending("balance")).limit(10).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                User user = mapDocumentToUser(doc);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }
}