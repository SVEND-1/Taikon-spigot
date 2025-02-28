package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class UserDB implements DAO<User, UUID> {
    private final MongoCollection<Document> userCollection;

    public UserDB(MongoDatabase database) {
        this.userCollection = database.getCollection("Players");
    }

    @Override
    public boolean insert(User user) {
        userCollection.insertOne(createUserDocument(user));
        return true;
    }

    @Override
    public User read(UUID id) {
        Document doc = userCollection.find(eq("_id", id.toString())).first();
        return doc != null ? mapDocumentToUser(doc) : null;
    }

    @Override
    public boolean update(User user) {
        UpdateResult result = userCollection.replaceOne(eq("_id", user.getId().toString()), createUserDocument(user));
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(User user) {
        DeleteResult result = userCollection.deleteOne(eq("_id", user.getId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<User> getAll() {
        return StreamSupport.stream(userCollection.find().spliterator(), false)
                .map(this::mapDocumentToUser)
                .collect(Collectors.toList());
    }

    private Document createUserDocument(User user) {
        return new Document("_id", user.getId().toString())
                .append("name", user.getName())
                .append("income", user.getIncome())
                .append("balance", user.getBalance())
                .append("incomeMultiplier", user.getIncomeMultiplier());
    }


    public User mapDocumentToUser(Document doc) {
        return new User(
                UUID.fromString(doc.getString("_id")),
                doc.getString("name"),
                doc.getLong("income"),
                doc.getLong("balance"),
                doc.getDouble("incomeMultiplier")
        );
    }
}