package db;

import org.bson.Document;

public class ItemFactory implements Factory<Item> {

    @Override
    public Item fromDocument(Document document) {
        return new Item(
                document.getInteger("id"),
                document.getString("name"),
                document.getDouble("price"),
                document.getString("currency")
        );
    }
}
