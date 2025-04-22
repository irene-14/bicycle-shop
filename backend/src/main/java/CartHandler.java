import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class CartHandler implements HttpHandler {
    private final DataLoader dataLoader = new DataLoader();  // Load both JSONs

    public void handle(HttpExchange exchange) throws IOException {
        List<Map<String, String>> cart = new ArrayList<>();

        if ("POST".equals(exchange.getRequestMethod())) {
            String body;
            try (InputStream is = exchange.getRequestBody();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                body = reader.lines().reduce("", (acc, cur) -> acc + cur);
            }
            
            Map<String, String> item = parseJson(body);
            cart.add(item);
            

            int price = calculatePrice(item);
            String jsonResponse = "{ \"total\": " + price + " }";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

    private int calculatePrice(Map<String, String> item) {
        int price = 0;

        // Get the product prices from the JSON
        Map<String, Map<String, Integer>> productPrices = dataLoader.getProductPrices();

        // Sum the base prices
        for (String key : item.keySet()) {
            Map<String, Integer> options = productPrices.get(key);
            if (options != null) {
                price += options.getOrDefault(item.get(key), 0);
            }
        }

        // Apply special pricing rules if applicable
        List<Map<String, Object>> specialPricingRules = dataLoader.getSpecialPricingRules();
        for (Map<String, Object> rule : specialPricingRules) {
            Map<String, String> condition =  (Map<String, String>) rule.get("condition");
            if (matchesCondition(item, condition)) {
                price += ((Double) rule.get("extraCost")).intValue();
            }
        }

        return price;
    }

    private boolean matchesCondition(Map<String, String> item, Map<String, String> condition) {
        for (Map.Entry<String, String> entry : condition.entrySet()) {
            if (!entry.getValue().equals(item.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> parseJson(String body) {
        Map<String, String> map = new HashMap<>();
        body = body.replaceAll("[{}]", "");
        for (String pair : body.split(",")) {
            String[] parts = pair.split(":");
            map.put(parts[0].trim(), parts[1].trim());
        }
        return map;
    }
}
