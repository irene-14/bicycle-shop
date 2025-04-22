import java.util.Map;

public class Bicycle {
    private Map<String, Part> parts;

    public Bicycle(Map<String, Part> parts) {
        this.parts = parts;
    }

    // Method to calculate total price of the bicycle
    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Part part : parts.values()) {
            totalPrice += part.calculatePrice();
        }
        return totalPrice;
    }

    public Map<String, Part> getParts() {
        return parts;
    }
}
