import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.util.*;

/**
 * Handles POST requests to /cart.
 * Accepts a JSON object representing selected options,
 * calculates total price using the preloaded parts data,
 * and returns the result as JSON.
 */
public class CartHandler implements HttpHandler {
    private final Map<String, Map<String, Integer>> partsData;
    private final List<Map<String, String>> cart = new ArrayList<>();

    public CartHandler(Map<String, Map<String, Integer>> partsData) {
        this.partsData = partsData;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Read JSON body from request
            String body;
            try (InputStream is = exchange.getRequestBody();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                body = reader.lines().reduce("", (acc, cur) -> acc + cur);
            }

            // Parse body into key-value map and add to cart
            Map<String, String> item = parseJson(body);
            cart.add(item);

            // Compute price for this configuration
            int price = calculatePrice(item);
            String jsonResponse = "{\"price\":" + price + "}";

            // Send price response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

    /**
     * Parses a simple flat JSON object (no nesting) into a Map.
     */
    private Map<String, String> parseJson(String body) {
        Map<String, String> map = new HashMap<>();
        body = body.replaceAll("[{}\"]", "");

        for (String pair : body.split(",")) {
            String[] parts = pair.split(":");
            if (parts.length == 2) {
                map.put(parts[0].trim(), parts[1].trim());
            }
        }
        return map;
    }

    /**
     * Calculates the total price of a cart item based on selected options and loaded parts data.
     */
    private int calculatePrice(Map<String, String> item) {
        int total = 0;

        for (Map.Entry<String, String> entry : item.entrySet()) {
            String partName = entry.getKey();
            String optionName = entry.getValue();

            Map<String, Integer> options = partsData.get(partName);
            if (options != null && options.containsKey(optionName)) {
                total += options.get(optionName);
            }
        }

        return total;
    }
}
