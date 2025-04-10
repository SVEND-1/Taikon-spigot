package svend.taikon.DataBase.ModelDAO.Buildings;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import svend.taikon.LargeNumber;
import svend.taikon.Model.Product;

public class ProductDB {
    private final MongoCollection<Document> productCollection;

    public ProductDB(MongoDatabase database) {
        this.productCollection = database.getCollection("Products");
    }

    public Document createProductDocument(Product product) {
        if(product == null)
            return null;

        try {
            return new Document()
                    .append("name", product.getName())
                    .append("price", product.getPrice().getValue().toString())
                    .append("count", product.getCount())
                    .append("lvl", product.getLvl())
                    .append("time", product.getTime())
                    .append("open", product.isOpen());
        }
        catch (Exception e){
            return null;
        }
    }

    public Product mapDocumentToProduct(Document doc) {
        if (doc == null) return null;

        try {
            return new Product(
                    doc.getString("name"),
                    new LargeNumber(doc.getString("price")),
                    doc.getInteger("count"),
                    doc.getInteger("lvl"),
                    doc.getInteger("time"),
                    doc.getBoolean("open")
            );
        }
        catch (Exception e){
            return null;
        }
    }
}