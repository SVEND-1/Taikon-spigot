package svend.taikon.DataBase.ModelDAO.Buildings;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import svend.taikon.DataBase.DAO;
import svend.taikon.LargeNumber;
import svend.taikon.Menu.BuildingsMenu.Settings.BuildingType;
import svend.taikon.Model.Buildings.Bakery;
import svend.taikon.Model.Buildings.Building;
import svend.taikon.Model.Buildings.Garden;
import svend.taikon.Model.Buildings.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;
import static svend.taikon.Menu.BuildingsMenu.Settings.BuildingType.BAKERY;


public abstract class BuildingDB<T extends Building> implements DAO<T, UUID> {
    protected final MongoCollection<Document> collection;
    protected final ProductDB productDB;
    private final Class<T> buildingClass;

    public BuildingDB(MongoDatabase database, String collectionName, Class<T> buildingClass) {
        this.collection = database.getCollection(collectionName);
        this.productDB = new ProductDB(database);
        this.buildingClass = buildingClass;
    }

    @Override
    public boolean insert(T building) {
        if (building == null) {
            return false;
        }

        try {
            Document doc = createDocument(building);
            if (doc == null) {
                return false;
            }
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static DAO<? extends Building, UUID> getBuildingDB(MongoDatabase database, BuildingType type) {
        switch (type) {
            case BAKERY:
                return new BakeryDB(database);
            case GARDEN:
                return new GardenDB(database);
            default:
                throw new IllegalArgumentException();
        }
    }
    @Override
    public T read(UUID userId) {
        try {
            Document doc = collection.find(eq("userId", userId.toString())).first();
            return doc != null ? mapDocumentToBuilding(doc) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean update(T building) {
        if (building == null) {
            return false;
        }

        try {
            Document doc = createDocument(building);
            if (doc == null) {
                return false;
            }
            UpdateResult result = collection.replaceOne(
                    eq("userId", building.getUserId().toString()),
                    doc
            );
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(T building) {
        if (building == null) {
            return false;
        }

        try {
            DeleteResult result = collection.deleteOne(eq("userId", building.getUserId().toString()));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<T> getAll() {
        List<T> result;
        try {
            result = StreamSupport.stream(collection.find().spliterator(), false)
                    .map(this::mapDocumentToBuilding)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            result = new ArrayList<>();
        }
        return result;
    }

    protected Document createDocument(T building) {
        if (building == null) {
            return null;
        }

        try {
            return new Document()
                    .append("name", building.getName())
                    .append("price", building.getPrice().getValue().toString())
                    .append("upIncome", building.getUpIncome().getValue().toString())
                    .append("level", building.getLevel())
                    .append("firstProduct", building.getFirstProduct() != null ?
                            productDB.createProductDocument(building.getFirstProduct()) : null)
                    .append("secondProduct", building.getSecondProduct() != null ?
                            productDB.createProductDocument(building.getSecondProduct()) : null)
                    .append("buildingsConstructed", building.isBuildingsConstructed())
                    .append("userId", building.getUserId().toString());
        } catch (Exception e) {
            return null;
        }
    }

    protected T mapDocumentToBuilding(Document doc) {
        if (doc == null) {
            return null;
        }

        try {
            Document firstProductDoc = doc.get("firstProduct", Document.class);
            Document secondProductDoc = doc.get("secondProduct", Document.class);

            if (buildingClass == Bakery.class) {
                return buildingClass.cast(new Bakery(
                        doc.getString("name"),
                        new LargeNumber(doc.getString("price")),
                        new LargeNumber(doc.getString("upIncome")),
                        doc.getInteger("level"),
                        firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                        secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                        doc.getBoolean("buildingsConstructed"),
                        UUID.fromString(doc.getString("userId"))
                ));
            } else if (buildingClass == Garden.class) {
                return buildingClass.cast(new Garden(
                        doc.getString("name"),
                        new LargeNumber(doc.getString("price")),
                        new LargeNumber(doc.getString("upIncome")),
                        doc.getInteger("level"),
                        firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                        secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                        doc.getBoolean("buildingsConstructed"),
                        UUID.fromString(doc.getString("userId"))
                ));
            }else if(buildingClass == Restaurant.class){
                return buildingClass.cast(new Restaurant(
                        doc.getString("name"),
                        new LargeNumber(doc.getString("price")),
                        new LargeNumber(doc.getString("upIncome")),
                        doc.getInteger("level"),
                        firstProductDoc != null ? productDB.mapDocumentToProduct(firstProductDoc) : null,
                        secondProductDoc != null ? productDB.mapDocumentToProduct(secondProductDoc) : null,
                        doc.getBoolean("buildingsConstructed"),
                        UUID.fromString(doc.getString("userId"))
                ));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}


