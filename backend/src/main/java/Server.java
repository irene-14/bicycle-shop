import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.*;

public class Server {
    private static List<Map<String, String>> cart = new ArrayList<>(); // Fix: List of <String, String>

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/products", new ProductsHandler());
        server.createContext("/cart", new CartHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }

    static class ProductsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("backend/src/main/data/products.json");
            String response = new String(Files.readAllBytes(file.toPath()));

            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class CartHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body;
                try (InputStream is = exchange.getRequestBody();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    body = reader.lines().reduce("", (acc, cur) -> acc + cur);
                }
                
                Map<String, String> item = parseJson(body);
                cart.add(item);                

                int price = calculatePrice(item);
                String jsonResponse = "{\"price\":" + price + "}";

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            }
        }

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

        private int calculatePrice(Map<String, String> item) {
            Map<String, Map<String, Integer>> prices = new HashMap<>();

            Map<String, Integer> frameTypePrices = new HashMap<>();
            frameTypePrices.put("Full-suspension", 130);
            frameTypePrices.put("Diamond", 90);
            frameTypePrices.put("Step-through", 70);
            prices.put("frameType", frameTypePrices);
            
            Map<String, Integer> frameFinishPrices = new HashMap<>();
            frameFinishPrices.put("Matte", 50);
            frameFinishPrices.put("Shiny", 30);
            prices.put("frameFinish", frameFinishPrices);
            
            Map<String, Integer> wheelsPrices = new HashMap<>();
            wheelsPrices.put("Road wheels", 80);
            wheelsPrices.put("Mountain wheels", 100);
            wheelsPrices.put("Fat bike wheels", 120);
            prices.put("wheels", wheelsPrices);
            
            Map<String, Integer> rimColorPrices = new HashMap<>();
            rimColorPrices.put("Red", 10);
            rimColorPrices.put("Black", 15);
            rimColorPrices.put("Blue", 20);
            prices.put("rimColor", rimColorPrices);
            
            Map<String, Integer> chainPrices = new HashMap<>();
            chainPrices.put("Single-speed chain", 43);
            chainPrices.put("8-speed chain", 60);
            prices.put("chain", chainPrices);
            
            // Calculate total price
            int price = 0;
            for (String key : item.keySet()) {
                Map<String, Integer> options = prices.get(key);
                if (options != null) {
                    price += options.getOrDefault(item.get(key), 0);
                }
            }            

            // Add custom combination bonus
            if ("Matte".equals(item.get("frameFinish")) && "Diamond".equals(item.get("frameType"))) {
                price += 5;
            }

            return price;
        }
    }
}
