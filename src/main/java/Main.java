import io.reactivex.netty.protocol.http.server.HttpServer;
import org.apache.log4j.BasicConfigurator;
import server.ReactiveHttpServer;

public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        HttpServer
                .newServer(8081)
                .start(ReactiveHttpServer::process)
                .awaitShutdown();
    }
}
