package db;

import org.bson.Document;

interface CommonEntity {

    int getId();

    Document toDocument();
}
