import java.util.Map;

public class Part {
    private String name;
    private Map<String, Option> options;
    private Option selectedOption;

    public Part(String name, Map<String, Option> options) {
        this.name = name;
        this.options = options;
    }

    // Select an option for the part
    public void selectOption(String optionName) {
        this.selectedOption = options.get(optionName);
    }

    // Calculate the price for this part
    public int calculatePrice() {
        if (selectedOption != null) {
            return selectedOption.getPrice();
        }
        return 0;
    }
    
    public String getName() {
        return name;
    }
}
