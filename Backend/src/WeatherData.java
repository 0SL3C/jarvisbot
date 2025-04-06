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
import java.io.File;
import java.util.Scanner;

public class WeatherData {

    static final String API_KEY = "be74631d6c15530aad0e592d5c66b18e";

    private static String loadApiKey() {
        String envKey = System.getenv("API_KEY");
    if (envKey != null && !envKey.isEmpty()) return envKey;

    try {
        Scanner scanner = new Scanner(new File("API_KEY"));
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
    } catch (Exception e) {
        System.out.println("❌ Não foi possível carregar a API_KEY: " + e.getMessage());
    }
    return null;
}
    public String city;
    public double temperature;
    public Date date;
    private JSONObject apiData;
    
    
    public WeatherData(){
        this.date = new Date();
    }
    
    public WeatherData(String city){
        this.city = city.trim().toLowerCase();
        this.date = new Date();
    }
    
    public WeatherData(String city, Date date){
        this.city = city;
        this.date = date;
    }
    
    public WeatherData(String city, Date date, String API_KEY){
        this.city = city;
        this.date = date;
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

       
    
        if (this.apiData == null || 
            !this.apiData.has("main") || 
            !this.apiData.has("weather")) {
            System.out.println("❌ Campos ausentes ou apiData nula.");
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
            if (this.city == null || this.city.isEmpty()) {
                System.out.println("❌ City is null or empty!");
                return null;
            }
    
            // 🔠 Normalize: remover espaços e forçar lowercase
            String normalized = this.city.trim().toLowerCase();
            String encodedCity = URLEncoder.encode(normalized, StandardCharsets.UTF_8);
    
            // 🔍 Verificação extra
            System.out.println("🌍 City requisitada: " + this.city);
            System.out.println("🌐 URL codificada: " + encodedCity);
    
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format(
                "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s",
                encodedCity, WeatherData.API_KEY
            );
    
            System.out.println("🔗 URL final: " + url); // 👈 Importantíssimo
    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("🔎 API Response Body: " + response.body());

            
            /// Analisa a resposta JSON
        JSONObject jsonObject = new JSONObject(response.body());

           // Verifica erro 404 (cidade não encontrada)
        if (jsonObject.has("cod") && jsonObject.get("cod").toString().equals("404")) {
            System.out.println("❌ Cidade não encontrada na API.");
            return null;
        }

        JSONArray forecastList = jsonObject.getJSONArray("list");
        JSONObject result = null;
        long closestTimeDifference = Long.MAX_VALUE;

        System.out.println("🔄 Procurando previsão mais próxima para: " + this.date);

        for (int i = 0; i < forecastList.length(); i++) {
            JSONObject iObj = forecastList.getJSONObject(i);
            long timestamp = iObj.getLong("dt") * 1000L;
            Date forecastDate = new Date(timestamp);


            System.out.println("⏰ Previsão disponível: " + forecastDate);

            long timeDifference = Math.abs(forecastDate.getTime() - this.date.getTime());

            if (timeDifference < closestTimeDifference) {
                closestTimeDifference = timeDifference;
                result = iObj;
            }
        }

        if (result == null) {
    System.out.println("⚠️ Nenhuma previsão próxima foi encontrada!");
} else {
    System.out.println("✅ Previsão selecionada: " + result.getString("dt_txt"));

        }

        return result;
    } catch(Exception e){
        e.printStackTrace();
        return null;
    }
  }
}