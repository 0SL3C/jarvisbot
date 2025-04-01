import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

import org.alicebot.ab.*;
import org.apache.http.util.EntityUtils;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chatws")
public class ChatWebSocket{

    private Bot bot;
    private Chat chat;
    private StringBuilder response;

    //////////////////////////////////////////////////////// WEB SOCKET HANDLING ////////////////////////////////////////////////////////
    @OnOpen
    public void onOpen(Session session){
        
        System.out.println("\n" + "#".repeat(24));
        System.out.println("NEW WEBSOCKET ESTABLISHED. ID: " + session.getId());
        System.out.println("#".repeat(24) + "\n");
        bot = new Bot("jarvis", "Backend/ab", "chat");
        bot.writeAIMLIFFiles();
        chat = new Chat(bot);
        try{
            session.getBasicRemote().sendText("Hello, how can I help you today?" + debug());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received: " + message);

        /// If weather request triggered
        if (message.startsWith("weather:")) {
            response = new StringBuilder("Forecast:");
            String[] locations = message.substring(8).split(","); // Fetch weather locations
            for (String location : locations){
                WeatherData cityWeather = getWeatherData(location, "be74631d6c15530aad0e592d5c66b18e");
                response.append(generateClothingSuggestions(cityWeather, location)).append("\n");
            }
            if (locations.length != 5){
                sendMessage(session, "Please enter the 5 locations separated by Comma");
            }
            sendMessage(session, response.toString());
            
        } else {
            String botResponse = chat.multisentenceRespond(message);
            sendMessage(session, botResponse);
        }
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("A websocket has been closed: " + session.getId());
    }
    @OnError
    public void onError(Session session, Throwable throwable){
        System.err.println("Websocket error: " + throwable.getMessage());
    }

   
//////////////////////////////////////////////////////// END WEB SOCKET HANDLING ////////////////////////////////////////////////////////

//////////////////////////////////////////////////////// METHODS ////////////////////////////////////////////////////////
    // private WeatherData getWeatherDataDISABLE(String city) {
    //     String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    //     // https://api.openweathermap.org/data/2.5/weather?q=cork&appid=be74631d6c15530aad0e592d5c66b18e&units=metric
    //     // OPEN HTTP CLIENT
    //     try{
    //         CloseableHttpClient client = HttpClients.createDefault();
    //         String url = String.format(WEATHER_URL, city, API_KEY); /// FORMAT STRING TO POST REQUEST
    //         HttpGet request = new HttpGet(url); /// POST REQUEST TO WEATHER API

    //         return client.execute(request, response -> {
    //             ObjectMapper mapper = new ObjectMapper();
    //             JsonNode jsonData = mapper.readTree(response.getEntity().getContent());

    //             Double temperature = jsonData.path("main").path("temp").asDouble();
    //             String conditions = jsonData.path("weather").get(0).path("description").asText();

    //             WeatherData data = new WeatherData(temperature, conditions);
    //             return data;
    //         });
    //     } catch (Exception e) {
    //         return new WeatherData();
    //     }
    // }

    String debug(){
        response = new StringBuilder();
        String[] locations = new String[] {"Chicago", "Boston", "Cork"};
        for (String location : locations) {
            try {
                WeatherData cityWeather = getWeatherData(location, "be74631d6c15530aad0e592d5c66b18e");
                response.append(generateClothingSuggestions(cityWeather, location)).append("\n");
            } catch (Exception e) {
                response.append("Error getting weather data for ").append(location).append(": ").append(e.getMessage()).append("\n");
            }
        }
        return response.toString();
    }

    public void sendMessage(Session session, String message){
        try{
            session.getBasicRemote().sendText(message);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public WeatherData getWeatherData(String city, String API_KEY) {
        try {
            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric", city, API_KEY);

            // Build the HTTP GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(response.body());
            Double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            String conditions = jsonObject.getJSONArray("weather")
                                        .getJSONObject(0)
                                        .getString("description");

            return new WeatherData(temperature, conditions);
        } catch (Exception e) {
            e.printStackTrace(); // Optional: for debugging
            return new WeatherData();
        }
    }

    //check if it is a climate request
    public String generateClothingSuggestions(WeatherData data, String city) {
        StringBuilder suggestions = new StringBuilder();
        Double temperature = data.getTemperature();
        String conditions = data.getConditions();
        String suggestionStr = String.format("📍 <strong>%s</strong> %.2f°C, %s<br>Recommended wear: ", city, temperature, conditions);
        suggestions.append(suggestionStr);
        // "📍 Cork 25.2°C, Sunny"
        // suggestions.append("📍 **").append(city).append("** (").append(data.getTemperature()).append("°C, ").append(data.getConditions()).append("):\n");
        if (temperature < 20 && temperature > 10) {
            suggestions.append("🧣 Light jacket, jeans<br><br>");
        }else if (temperature < 10 ) {
            suggestions.append("🧥 Heavy coat, gloves, scarf<br><br>");
        }else {
            suggestions.append("👕 T-Shirt, shorts<br><br>");
        }
        if (conditions.toLowerCase().contains("rain")) { // "rain" em inglês
            suggestions.append("🌂 Take an umbrella!<br>");
        }
        if (conditions.isEmpty()) {
            return "📍 **" + city + "**: Data not found ❌";
        }
            return suggestions.toString();
    }
}