package svend.taikon.DataBase.ModelDAO.Buildings;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bukkit.Bukkit;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Garden;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

public class BakeryDB extends BuildingDB<Bakery> {
    public BakeryDB(MongoDatabase database) {
        super(database, "Bakery", Bakery.class);
    }
}