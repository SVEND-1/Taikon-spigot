package svend.taikon.DataBase.ModelDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.entity.Player;
import svend.taikon.DataBase.DAO;
import svend.taikon.Model.User;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class UserDB implements DAO<User,UUID> {
    private final MongoCollection<Document> userCollection;

    public UserDB(MongoDatabase database) {
        this.userCollection = database.getCollection("Players");
    }

    @Override
    public boolean insert(User user) {
        // Создаем документ с UUID, именем, доходом и балансом
        Document doc = new Document("_id", user.getId().toString()) // Используем _id для UUID
                .append("name", user.getName())
                .append("income", user.getIncome())
                .append("balance", user.getBalance());

        userCollection.insertOne(doc);
        return true;
    }

    @Override
    public User read(UUID id) {
        Document doc = userCollection.find(eq("_id", id.toString())).first();
        if (doc != null) {
            return mapDocumentToUser(doc);
        }
        return null;
    }

    @Override
    public boolean update(User user) {
        // Создаем документ для обновления
        Document doc = new Document("_id", user.getId().toString())
                .append("name", user.getName())
                .append("income", user.getIncome())
                .append("balance", user.getBalance());

        // Обновляем документ
        UpdateResult result = userCollection.replaceOne(eq("_id", user.getId().toString()), doc);
        return result.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(User user) {
        // Удаляем документ по UUID
        DeleteResult result = userCollection.deleteOne(eq("_id", user.getId().toString()));
        return result.getDeletedCount() > 0;
    }

    @Override
    public List<User> getAll() {
        // Получаем все документы и преобразуем их в список User
        List<User> users = new ArrayList<>();
        for (Document doc : userCollection.find()) {
            users.add(mapDocumentToUser(doc));
        }
        return users;
    }

    @Override
    public User mapDocumentToUser(Document doc) {
        User user = new User();
        user.setId(UUID.fromString(doc.getString("_id"))); // Преобразуем строку в UUID
        user.setName(doc.getString("name"));
        user.setIncome(doc.getLong("income"));
        user.setBalance(doc.getLong("balance"));
        return user;
    }


}
