import org.alicebot.ab.*;

public class MessageHandler {
    private Chat chat;

    /// Initiate MessageHandler with Web Socket Session
    public MessageHandler(Chat chat){
        this.chat = chat;
    }

    public String processMessage(String message){
        StringBuilder response;
        String botResponse = chat.multisentenceRespond(message);
        
        if(message.toLowerCase().startsWith("weather:")){
            response = new StringBuilder("Forecast:<br>");
            String[] locations = message.substring(8).split(","); // Fetch weather locations and separate them by comma (*, *)

            for (String location : locations){
                location = location.trim();

                WeatherData cityWeather = new WeatherData(location);
                String date = cityWeather.date.toString();
                // Capitalize the city name
                response.append(String.format("%s%s on %s: ", location.substring(0, 1).toUpperCase(), location.substring(1), date));                                

                cityWeather.setApiKey("be74631d6c15530aad0e592d5c66b18e");

                response.append(cityWeather.genCloth()).append("\n<br><br>");
            }
            return response.toString();
        }
        return botResponse.toString();
    }
}