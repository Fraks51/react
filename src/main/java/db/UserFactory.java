package db;

import org.bson.Document;

public class UserFactory implements Factory<User> {
    @Override
    public User fromDocument(Document document) {
        return new User(
                document.getInteger("id"),
                document.getString("name"),
                document.getString("currency")
        );
    }
}
