import java.io.*;
import java.util.*;

/**
 * Utility class for loading product configuration and pricing from a JSON file.
 */
public class DataLoader {

    /**
     * Loads the product parts and their option prices from a structured JSON file.
     * Expects structure: { "parts": { "<part>": { "options": { "<option>": <price>, ... } }, ... } }
     */
    public static Map<String, Map<String, Integer>> loadProductPrices(String filePath) {
        Map<String, Map<String, Integer>> parts = new HashMap<>();
        StringBuilder jsonContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            String json = jsonContent.toString()
                .replaceAll("[{}\"]", "")
                .replace("parts:", "");

            String[] partSections = json.split("options:");

            for (int i = 1; i < partSections.length; i++) {
                String[] lines = partSections[i].split(",");
                String partName = extractPartName(partSections[i - 1]);

                Map<String, Integer> options = new HashMap<>();
                for (String lineItem : lines) {
                    String[] entry = lineItem.trim().split(":");
                    if (entry.length == 2) {
                        options.put(entry[0].trim(), Integer.parseInt(entry[1].trim()));
                    }
                }
                parts.put(partName, options);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parts;
    }

    /**
     * Extracts the part name from the section preceding the "options:" block.
     */
    private static String extractPartName(String text) {
        String[] tokens = text.split(",");
        return tokens[tokens.length - 1].trim();
    }
}
