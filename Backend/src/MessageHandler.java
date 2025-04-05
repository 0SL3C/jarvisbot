import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.alicebot.ab.*;

public class MessageHandler {
    private Chat chat;

    public MessageHandler(){
    }

    /// Initiate MessageHandler with Web Socket Session
    public MessageHandler(Chat chat){
        this.chat = chat;
    }

    public String processMessage(String message){
        StringBuilder response;
        
        String botResponse = chat.multisentenceRespond(message);
        
        if(botResponse.toLowerCase().startsWith("weather:")){
            response = new StringBuilder("Forecast for\n<br>");

            // Fetch weather locations and separate them by comma (city:dow, city:dow)
            String[] locationDayPairs = botResponse.substring(8).split(","); 

            for (String pair : locationDayPairs) {
                String[] parts = pair.split(":");
                if (parts.length != 2) {
                    response.append("Invalid format for: ").append(pair).append("<br>");
                    continue;
                }
                String location = parts[0].trim();
                String day = parts[1].trim();

                // Convert day string to Date
                Date targetDate = convertDayToDate(day);
                WeatherData cityWeather = new WeatherData(location, targetDate);

                // Capitalize the city name and include the day
                response.append(String.format("%s%s on %s: ", 
                location.substring(0, 1).toUpperCase(), location.substring(1), day));
                response.append(cityWeather.genCloth()).append("<br><br>");
            }
            return response.toString();
        }
        return botResponse.toString();
    }

    // Convert day string (e.g., "Monday") to a Date relative to today (April 3, 2025)
    private Date convertDayToDate(String day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // Set to today: April 3, 2025
        cal.set(Calendar.HOUR_OF_DAY, 12); // Noon to avoid edge cases
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String todayDay = sdf.format(cal.getTime()).toLowerCase();
        String targetDay = day.trim().toLowerCase();

        int daysToAdd = 0;
        String[] daysOfWeek = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        int todayIndex = Arrays.asList(daysOfWeek).indexOf(todayDay);
        int targetIndex = Arrays.asList(daysOfWeek).indexOf(targetDay);

        if (targetIndex < 0) {
            return cal.getTime(); // Invalid day, return today
        }

        daysToAdd = (targetIndex - todayIndex + 7) % 7; // Ensure positive difference within a week
        cal.add(Calendar.DAY_OF_YEAR, daysToAdd);

        return cal.getTime();
    }
}