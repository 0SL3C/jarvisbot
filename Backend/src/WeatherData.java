import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData {
    public String API_KEY = System.getenv("API_KEY");
    public void setAPI_KEY(String API_KEY){
        this.API_KEY = API_KEY;
    }
    public String city;
    public Date date;
    private JSONObject apiData;

    public WeatherData() {
        this.date = new Date();
    }

    public WeatherData(String city) {
        this.city = city.trim().toLowerCase();
        this.date = new Date();
    }

    public WeatherData(String city, Date date) {
        this.city = city.trim().toLowerCase();
        this.date = date;
    }

    public void setCity(String city) {
        this.city = city.trim().toLowerCase();
        this.apiData = null; // Reset API data when city changes
    }

    public void setDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        this.setDate(date);
    }

    public void setDate(Date date) {
        this.date = date;
        this.apiData = null; // Reset API data when date changes
    }

    public double getTemperature() {
        try {
            if (this.apiData == null) {
                this.apiData = this.getAPIdata();
            }
            if (this.apiData == null) {
                return 0;
            }
            return this.apiData.getJSONObject("main").getDouble("temp");
        } catch (Exception e) {
            System.err.println("Error getting temperature: " + e.getMessage());
            return 0;
        }
    }

    public String getDescription() {
        try {
            if (this.apiData == null) {
                this.apiData = this.getAPIdata();
            }
            if (this.apiData == null) {
                return "No data available";
            }
            return this.apiData.getJSONArray("weather").getJSONObject(0).getString("description");
        } catch (Exception e) {
            System.err.println("Error getting description: " + e.getMessage());
            return "No data available";
        }
    }

    public String genCloth() {
        if (this.apiData == null) {
            this.apiData = this.getAPIdata();
        }

        if (this.apiData == null || !this.apiData.has("main") || !this.apiData.has("weather")) {
            return String.format("City not found or no forecast available for %s", this.city);
        }

        double temperature = this.getTemperature();
        String description = this.getDescription();

        if (temperature > 20) {
            return String.format("%.2f°C. Warm day, %s.<br>You should wear shorts 🩳 and T-Shirts 👕", temperature, description);
        } else if (temperature >= 10) {
            return String.format("%.2f°C. Mild day, %s.<br>You should consider bringing a coat 🧥", temperature, description);
        } else {
            return String.format("%.2f°C. Cold day, %s.<br>You should <strong>definitely</strong> bring a coat 🧥", temperature, description);
        }
    }

    public JSONObject getAPIdata() {
        try {
            if (this.city == null || this.city.isEmpty()) {
                System.err.println("City is null or empty!");
                return null;
            }

            // Normalize and encode city name
            String encodedCity = URLEncoder.encode(this.city.trim().toLowerCase(), StandardCharsets.UTF_8);

            // Build API request
            HttpClient client = HttpClient.newHttpClient();
            String url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&APPID=%s", encodedCity, this.API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            // Check for API errors (e.g., city not found)
            if (jsonObject.has("cod") && !jsonObject.getString("cod").equals("200")) {
                System.err.println("API error: " + jsonObject.getString("message"));
                return null;
            }

            // Get forecast list
            JSONArray forecastList = jsonObject.getJSONArray("list");

            // Normalize target date to midnight for comparison
            Calendar targetCal = Calendar.getInstance();
            targetCal.setTime(this.date);
            targetCal.set(Calendar.HOUR_OF_DAY, 0);
            targetCal.set(Calendar.MINUTE, 0);
            targetCal.set(Calendar.SECOND, 0);
            targetCal.set(Calendar.MILLISECOND, 0);
            long targetMidnight = targetCal.getTimeInMillis();

            // Check if date is within 5-day forecast period
            Calendar maxForecastCal = Calendar.getInstance();
            maxForecastCal.setTime(new Date());
            maxForecastCal.add(Calendar.DAY_OF_YEAR, 5);
            maxForecastCal.set(Calendar.HOUR_OF_DAY, 23);
            maxForecastCal.set(Calendar.MINUTE, 59);
            maxForecastCal.set(Calendar.SECOND, 59);
            if (targetCal.after(maxForecastCal)) {
                System.err.println("Date is beyond 5-day forecast period: " + this.date);
                return null;
            }

            // Find forecast closest to noon on the target date
            JSONObject closestForecast = null;
            long closestTimeDifference = Long.MAX_VALUE;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);
                long timestamp = forecast.getLong("dt") * 1000L;
                Date forecastDate = new Date(timestamp);

                // Check if forecast is for the same day
                if (sdf.format(forecastDate).equals(sdf.format(this.date))) {
                    // Calculate time difference to noon
                    Calendar forecastCal = Calendar.getInstance();
                    forecastCal.setTime(forecastDate);
                    forecastCal.set(Calendar.HOUR_OF_DAY, 12); // Noon
                    forecastCal.set(Calendar.MINUTE, 0);
                    forecastCal.set(Calendar.SECOND, 0);
                    long timeDifference = Math.abs(forecastDate.getTime() - forecastCal.getTimeInMillis());

                    if (timeDifference < closestTimeDifference) {
                        closestTimeDifference = timeDifference;
                        closestForecast = forecast;
                    }
                }
            }

            if (closestForecast == null) {
                System.err.println("No forecast found for " + this.city + " on " + sdf.format(this.date));
                return null;
            }

            return closestForecast;
        } catch (Exception e) {
            System.err.println("Error fetching API data for " + this.city + ": " + e.getMessage());
            return null;
        }
    }
}