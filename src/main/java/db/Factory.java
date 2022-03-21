package db;

import org.bson.Document;

public interface Factory<E extends CommonEntity> {
    E fromDocument(Document document);
}
