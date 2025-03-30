public class WeatherData {
    private double temperature = 0.0;
    private String conditions = "";
    private String city = "";

    public WeatherData(){
    }

    public WeatherData(double temperature, String conditions) {
        this.temperature = temperature;
        this.conditions = conditions;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }



    public String getConditions() {
        return conditions;
    }



    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public static void main(String[] args) {
    WeatherData data = new WeatherData();
    data.setTemperature(25.5);
    data.setConditions("Sunny");
    System.out.println("Temperature: " + data.getTemperature());
    System.out.println("Conditions: " + data.getConditions());
}

}
