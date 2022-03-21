package server;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.netty.buffer.ByteBuf;
import rx.Observable;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rx.Observable;

import java.util.Map;

public class ReactiveHttpServer {

    public static Observable<Void> process(
            HttpServerRequest<ByteBuf> request,
            HttpServerResponse<ByteBuf> response
    ) {
        var requestCommand = request.getDecodedPath();
        var query = request.getQueryParameters();
        Map.Entry<HttpResponseStatus, Observable<String>> result;
        switch (requestCommand) {
            case "/get_all_users":
                result = Controller.getAllUsers(query);
                break;
            case "/get_all_items":
                result = Controller.getAllItems(query);
                break;
            case "/add_user":
                result = Controller.addUser(query);
                break;
            case "/add_item":
                result = Controller.addItem(query);
                break;
            case "/get_user":
                result = Controller.getUserById(query);
                break;
            case "/get_item":
                result = Controller.getItemById(query);
                break;
            case "/get_user_items":
                result = Controller.getAllItemsForUser(query);
                break;
            default:
                response.setStatus(HttpResponseStatus.BAD_REQUEST);
                return response.writeString(Observable.just("Wrong command:" + requestCommand));
        }
        response.setStatus(result.getKey());
        return response.writeString(result.getValue());
    }

}
