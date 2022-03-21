package server;

import db.ItemEntity;
import db.ReactiveDAO;
import db.UserEntity;
import io.netty.handler.codec.http.HttpResponseStatus;
import rx.Observable;

import java.awt.event.ItemEvent;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Controller {
    static public Map.Entry<HttpResponseStatus, Observable<String>> checkAndExecute(
            Map<String, List<String>> q,
            Map<String, Class<?>> required,
            Function<Map<String, List<String>>, Observable<String>> action
    ) {
        for (var tmpValue : required.entrySet()) {
            var key = tmpValue.getKey();
            var value = tmpValue.getValue();
            if (!q.containsKey(key)) {
                return Map.entry(
                        HttpResponseStatus.BAD_REQUEST,
                        Observable.just("No parameter " + key)
                );
            }
            if (value.isAssignableFrom(Integer.class)) {
                try {
                    Integer tmp = Integer.parseInt(q.get(key).get(0));
                } catch (Exception e) {
                    return Map.entry(
                            HttpResponseStatus.BAD_REQUEST,
                            Observable.just("Can't parse int")
                    );
                }
            }
            if (value.isAssignableFrom(Double.class)) {
                try {
                    Double tmp = Double.parseDouble(q.get(key).get(0));
                } catch (Exception e) {
                    return Map.entry(
                            HttpResponseStatus.BAD_REQUEST,
                            Observable.just("Can't parse double")
                    );
                }
            }
        }
        return Map.entry(
                HttpResponseStatus.OK,
                action.apply(q)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> addUser(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(
                        "id", Integer.class,
                        "name", String.class,
                        "currency", Currency.class
                ),
                mapQ -> {
                    var tmp = ReactiveDAO.userDao.add(
                            new UserEntity(
                                    Integer.parseInt(mapQ.get("id").get(0)),
                                    mapQ.get("name").get(0),
                                    mapQ.get("currency").get(0)
                            )
                    );
                    return tmp.map(Object::toString);
                }
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> addItem(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(
                        "id", Integer.class,
                        "name", String.class,
                        "price", Double.class,
                        "currency", Currency.class
                ),
                mapQ -> ReactiveDAO.itemDao.add(
                        new ItemEntity(
                                Integer.parseInt(mapQ.get("id").get(0)),
                                mapQ.get("name").get(0),
                                Double.parseDouble(mapQ.get("price").get(0)),
                                mapQ.get("currency").get(0)
                        )
                ).map(Object::toString)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> getUserById(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of("id", Integer.class),
                mapQ -> ReactiveDAO.userDao.getById(
                        Integer.parseInt(mapQ.get("id").get(0))
                ).map(Object::toString)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> getItemById(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(),
                mapQ -> ReactiveDAO.itemDao.getById(
                        Integer.parseInt(q.get("id").get(0))
                ).map(Object::toString)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> getAllUsers(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(),
                mapQ -> ReactiveDAO.userDao.getAll().map(Object::toString)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> getAllItems(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(),
                mapQ -> ReactiveDAO.itemDao.getAll().map(Object::toString)
        );
    }

    public static Map.Entry<HttpResponseStatus, Observable<String>> getAllItemsForUser(Map<String, List<String>> q) {
        return checkAndExecute(
                q, Map.of(),
                mapQ -> ReactiveDAO.getItemsForUser(
                        Integer.parseInt(q.get("id").get(0))
                ).map(Object::toString)
        );
    }
}
