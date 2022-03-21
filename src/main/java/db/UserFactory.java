package db;

import org.bson.Document;

public class UserFactory implements Factory<UserEntity> {
    @Override
    public UserEntity fromDocument(Document document) {
        return new UserEntity(
                document.getInteger("id"),
                document.getString("name"),
                document.getString("currency")
        );
    }
}
