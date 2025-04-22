import java.util.HashMap;
import java.util.Map;

public class Price {
    private Map<String, Map<String, Integer>> prices;

    public Price(Map<String, Map<String, Integer>> prices) {
        this.prices = prices;
    }

    // Calculate the price for a part based on its selected option
    public int calculatePartPrice(String partType, String optionName) {
        return prices.getOrDefault(partType, new HashMap<>()).getOrDefault(optionName, 0);
    }

    // Apply special cases and calculate final price
    public int applySpecialPrice(Map<String, String> selectedOptions, int currentPrice) {
        // Apply any special pricing rules here
        // For example, if "Matte" is selected for a "Full-suspension" frame:
        if ("Matte".equals(selectedOptions.get("frameFinish")) && "Full-suspension".equals(selectedOptions.get("frameType"))) {
            currentPrice += 5;
        }
        return currentPrice;
    }
}
