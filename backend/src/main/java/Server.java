import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.*;

public class Server {
    public static void main(String[] args) throws Exception {
        // Load product data from the JSON file into memory at server startup
        Map<String, Map<String, Integer>> partsData = DataLoader.loadProductPrices("backend/src/main/data/products.json");

        // Create an HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Register handlers for the /products and /cart endpoints
        server.createContext("/products", new ProductsHandler("backend/src/main/data/products.json"));
        server.createContext("/cart", new CartHandler(partsData));

        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
