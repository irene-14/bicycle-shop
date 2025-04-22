import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.Files;

/**
 * Handles GET requests to /products by returning the contents of the products.json file.
 */
public class ProductsHandler implements HttpHandler {
    private final String filePath;

    public ProductsHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read the product data from the JSON file
        File file = new File(filePath);
        String response = new String(Files.readAllBytes(file.toPath()));

        // Set response headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        // Send the JSON data as response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
