package db;

import org.bson.Document;

import java.util.Map;

public class Item implements CommonEntity {

    public Integer id;
    public String name;
    public Double price;
    public String currency;

    public Item(Integer id, String name, Double price, String currency) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency = currency;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Document toDocument() {
        return new Document(Map.of(
                "id", id,
                "name", name,
                "price", price,
                "currency", currency));
    }
}
