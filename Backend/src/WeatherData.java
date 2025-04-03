import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData {
    public String city;
    public double temperature;
    public Date date;
    private String API_KEY;
    private JSONObject apiData;
    
    public WeatherData(){
        this.date = new Date();
    }
    
    public WeatherData(String city){
        this.city = city;
        this.date = new Date();
    }
    
    public WeatherData(String city, Date date){
        this.city = city;
        this.date = date;
    }
    
    public WeatherData(String city, Date date, String API_KEY){
        this.city = city;
        this.date = date;
        this.API_KEY = API_KEY;
    }
    
    public void setApiKey(String API_KEY){
        this.API_KEY = API_KEY;
        this.apiData = this.getAPIdata();
    }
    
    public void setCity(String city){this.city = city.toLowerCase();}

    public void setDate(String dateString) throws Exception{
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        this.setDate(date);
    }
    public void setDate(Date date)  {this.date = date;}

    public double getTemperature(){
        try{
            if(this.apiData == null){
                this.apiData = this.getAPIdata();
            }
            return this.apiData.getJSONObject("main").getDouble("temp");
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public String getDescription(){
        try{
            if(this.apiData == null){
                this.apiData = this.getAPIdata();
            }
            return this.apiData.getJSONArray("weather").getJSONObject(0).getString("description");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String genCloth(){
        // Ensure API data is fetched
        if (this.apiData == null) {
            this.apiData = this.getAPIdata();
        }

        // If API data is still null, return city not found message
        if (this.apiData == null) {
            return String.format("City not found: %s", this.city);
        }

        double temperature = this.getTemperature();
        String description = this.getDescription();
        if(temperature > 20){
            return String.format("%.2f°C. Warm day, %s.<br>You should wear shorts 🩳 and T-Shirts 👕",temperature, description);
        }else if(temperature < 20 && temperature > 10){
            return String.format("%.2f°C. Mild day, %s.<br>You should consider bringing a coat 🧥",temperature, description);
        }else {
            return String.format("%.2f°C. Cold day, %s.<br>You should <strong>definitely</strong> bring a coat🧥",temperature, description);
        }
    }

    public JSONObject getAPIdata(){
        try {
            // Encode the city name to handle spaces and special characters
            String encodedCity = URLEncoder.encode(this.city, StandardCharsets.UTF_8);

            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s", encodedCity, this.API_KEY);

            System.out.println(url);
            // Build the HTTP GET request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            

            // Send the request and store the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response and return it
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray forecastList = jsonObject.getJSONArray("list");

            // Initialize the JSONObject
            JSONObject result = null;

            /// Iterator to find the closest date
            long closestTimeDifference = Long.MAX_VALUE;
            for(int i=0; i < forecastList.length(); i++){
                JSONObject iObj = forecastList.getJSONObject(i);
                Date iDate = new Date(iObj.getLong("dt"));

                long timeDifference = Math.abs(iDate.getTime() - this.date.getTime());
                if(timeDifference < closestTimeDifference){
                    closestTimeDifference = timeDifference;
                    result = iObj;
                }
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
