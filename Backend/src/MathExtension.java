import org.alicebot.ab.*;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Set;

public class MathExtension implements AIMLProcessorExtension {
    
    @Override
    public Set<String> extensionTagSet() {
        // Return the tag names this extension handles
        Set<String> result = new HashSet<String>();
        result.add("math");
        return result;
    }
    
    @Override
    public String recursEval(Node node, ParseState parseState) {
        try {
            // Get the content from the node
            String expression = AIMLProcessor.evalTagContent(node, parseState, null);
            return processExpression(expression);
        } catch (Exception e) {
            return "Error in math extension: " + e.getMessage();
        }
    }
    
    private String processExpression(String input) {
        try {
            // Parse input like "add 5 3" or "multiply 6 7"
            String[] parts = input.trim().split("\\s+");
            if (parts.length < 3) {
                return "Invalid format. Please use: operation number1 number2";
            }
            
            String operation = parts[0].toLowerCase();
            double num1 = Double.parseDouble(parts[1]);
            double num2 = Double.parseDouble(parts[2]);
            double result = 0;
            
            switch (operation) {
                case "add":
                    result = num1 + num2;
                    break;
                case "subtract":
                    result = num1 - num2;
                    break;
                case "multiply":
                    result = num1 * num2;
                    break;
                case "divide":
                    if (num2 == 0) return "Cannot divide by zero";
                    result = num1 / num2;
                    break;
                default:
                    return "Unknown operation: " + operation;
            }
            
            // Format result - show as integer if it's a whole number
            if (result == (int) result) {
                return String.valueOf((int) result);
            } else {
                return String.valueOf(result);
            }
        } catch (NumberFormatException e) {
            return "Invalid numbers in the calculation";
        } catch (Exception e) {
            return "Error processing math operation: " + e.getMessage();
        }
    }
}