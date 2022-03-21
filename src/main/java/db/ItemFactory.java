package db;

import org.bson.Document;

public class ItemFactory implements Factory<ItemEntity> {

    @Override
    public ItemEntity fromDocument(Document document) {
        return new ItemEntity(
                document.getInteger("id"),
                document.getString("name"),
                document.getDouble("price"),
                document.getString("currency")
        );
    }
}
