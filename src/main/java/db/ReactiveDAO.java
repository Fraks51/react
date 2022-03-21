package db;

import com.mongodb.rx.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoDatabase;
import rx.Observable;
import currency.Currency;

public class ReactiveDAO {

    static public class CommonDAO<E extends CommonEntity> {

        public String collection;
        public MongoDatabase db;
        public Factory<E> factory;

        public CommonDAO(MongoDatabase db, String collection, Factory<E> factory) {
            this.db = db;
            this.collection = collection;
            this.factory = factory;
        }

        public Observable<E> getAll() {
            return db.getCollection(collection)
                    .find()
                    .toObservable()
                    .map(factory::fromDocument);
        }

        public Observable<E> getById(int id) {
            Observable<E> id1 = db
                    .getCollection(collection)
                    .find(Filters.eq("id", id))
                    .toObservable()
                    .first()
                    .map(factory::fromDocument);
            return id1;
        }

        public Observable<String> add(E entity) {
            return getById(entity.getId())
                    .singleOrDefault(null)
                    .flatMap(found -> {
                        if (found == null) {
                            return db
                                    .getCollection(collection)
                                    .insertOne(entity.toDocument())
                                    .asObservable()
                                    .isEmpty()
                                    .flatMap(it -> {
                                        if (it) {
                                            return Observable.just("Failed");
                                        } else {
                                            return Observable.just("Completed");
                                        }
                                    });
                        } else {
                            return Observable.just("Failed");
                        }
                    });
        }

    }

    static public final MongoDatabase db = MongoClients
            .create("mongodb://localhost:27017")
            .getDatabase("reactive");

    static public final CommonDAO<User> userDao = new CommonDAO<>(db, "user", new UserFactory());
    static public final CommonDAO<Item> itemDao = new CommonDAO<>(db, "item", new ItemFactory());

    static public Observable<Item> getItemsForUser(int userId) {
        return userDao.getById(userId).flatMap(user ->
            itemDao.getAll().map(
                    item -> {
                        var newPrice = Currency.toNewPrice(item.price, item.currency, user.currency);
                        return new Item(item.id, item.name, newPrice, user.currency);
                    }
            )
        );
    }

}
