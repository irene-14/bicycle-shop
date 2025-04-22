import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;

import java.util.*;

public class DataLoader {
    private Map<String, Map<String, Integer>> productPrices;
    private List<Map<String, Object>> specialPricingRules;

    public DataLoader() {
        productPrices = loadProductPrices();
        specialPricingRules = loadSpecialPricingRules();
    }

    private Map<String, Map<String, Integer>> loadProductPrices() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("backend/src/main/data/products.json")));
            Gson gson = new Gson();
            return gson.fromJson(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private List<Map<String, Object>> loadSpecialPricingRules() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("backend/src/main/data/special-pricing.json")));
            Gson gson = new Gson();
            return gson.fromJson(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Map<String, Map<String, Integer>> getProductPrices() {
        return productPrices;
    }

    public List<Map<String, Object>> getSpecialPricingRules() {
        return specialPricingRules;
    }
}
