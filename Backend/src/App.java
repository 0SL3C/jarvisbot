import org.alicebot.ab.*;
import org.alicebot.ab.utils.IOUtils;

public class App {
    private static Chat chatSession;

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        String abPath = "ab";
        String botName = "jarvis";
        String action = "chat";

        Bot bot = new Bot(botName, abPath, action);
        chatSession = new Chat(bot);

        while (true) {
            System.out.print("Message: ");
            String textLine = IOUtils.readInputTextLine();
            if (textLine == null || textLine.equals("q")) {
                bot.writeQuit();
                System.exit(0);
            }
            String response = processInput(textLine);
            System.out.println(botName + ": " + response);
        }
    }

    private static String processInput(String input) {
        String response = chatSession.multisentenceRespond(input);
        if (response.equals("CALCULATION_RESULT")) {
            String num1 = chatSession.predicates.get("num1");
            String operator = chatSession.predicates.get("operator");
            String num2 = chatSession.predicates.get("num2");
            return calculate(num1, operator, num2);
        }
        return response;
    }

    public static String calculate(String num1, String operator, String num2) {
        try {
            double number1 = Double.parseDouble(num1);
            double number2 = Double.parseDouble(num2);
            double result;

            switch (operator.toLowerCase()) {
                case "+":
                case "plus":
                    result = number1 + number2;
                    break;
                case "-":
                case "minus":
                    result = number1 - number2;
                    break;
                case "*":
                case "times":
                    result = number1 * number2;
                    break;
                case "/":
                case "divided by":
                    if (number2 == 0) {
                        return "Cannot divide by zero!";
                    }
                    result = number1 / number2;
                    break;
                default:
                    return "Unsupported operation: " + operator;
            }

            if (result == Math.floor(result)) {
                return String.valueOf((int) result);
            }
            return String.valueOf(result);

        } catch (NumberFormatException e) {
            return "Please provide valid numbers!";
        }
    }
}