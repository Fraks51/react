package db;

import org.bson.Document;

import java.util.Map;

public class UserEntity implements CommonEntity {
    public int id;
    public String name;
    public String currency;

    public UserEntity(Integer id, String name, String currency) {
        this.id = id;
        this.name = name;
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
                "currency", currency)
        );
    }
}
