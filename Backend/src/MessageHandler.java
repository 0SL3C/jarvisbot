import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.alicebot.ab.*;

public class MessageHandler {
    private Chat chat;

    public MessageHandler() {
    }

    public MessageHandler(Chat chat) {
        this.chat = chat;
    }

    public String processMessage(String message) {
        StringBuilder response;

        String botResponse = chat.multisentenceRespond(message);

        if (message.toLowerCase().startsWith("weather:")) {
            botResponse = message;
        }

        if (botResponse.toLowerCase().startsWith("weather:")) {
            response = new StringBuilder("Forecast for:<br>");

            String[] locationDayPairs = botResponse.substring(8).split(",");

            if (locationDayPairs.length > 5) {
                return "⚠️ Limit to 5 cities, how did you bypass the frontend limit?<br>";
            }

            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat prettyFormat = new SimpleDateFormat("EEEE, MMMM d");
            Date today = new Date();
            Calendar maxDateCal = Calendar.getInstance();
            maxDateCal.setTime(today);
            maxDateCal.add(Calendar.DAY_OF_YEAR, 5);
            Date maxDate = maxDateCal.getTime();

            for (String pair : locationDayPairs) {
                String[] parts = pair.split(":");
                if (parts.length != 2) {
                    response.append("⚠️ Invalid format: <strong>")
                            .append(pair)
                            .append("</strong><br>");
                    continue;
                }

                String location = parts[0].trim();
                String day = parts[1].trim();

                try {
                    // Parse the YYYY-MM-DD date
                    Date targetDate = dateParser.parse(day);

                    // Validate against past dates
                    if (targetDate.before(today)) {
                        response.append("⚠️ Cannot predict the past for <strong>")
                                .append(location)
                                .append(" on ")
                                .append(day)
                                .append("</strong>.<br>");
                        continue;
                    }

                    // Validate against 5-day limit
                    if (targetDate.after(maxDate)) {
                        response.append("⚠️ The API is limited to 5-day forecasts only. Today is ")
                                .append(prettyFormat.format(today))
                                .append(", you can request up to ")
                                .append(prettyFormat.format(maxDate))
                                .append(".<br>");
                        continue;
                    }

                    // Capitalize city name
                    String normalizedCity = location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();

                    // Create WeatherData with the parsed date
                    WeatherData cityWeather = new WeatherData(normalizedCity, targetDate);

                    // Format the date for display
                    String formattedDate = prettyFormat.format(targetDate);

                    // Build the response
                    response.append(String.format("%s on %s: ", normalizedCity, formattedDate));
                    response.append(cityWeather.genCloth()).append("<br><br>");
                } catch (ParseException e) {
                    response.append("⚠️ Invalid date format for <strong>")
                            .append(location)
                            .append(": ")
                            .append(day)
                            .append("</strong>.<br>");
                }
            }

            return response.toString();
        }

        return botResponse;
    }
}