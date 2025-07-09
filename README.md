# WeatherCare - Intelligent Weather Chatbot

**An AI-powered weather assistant that helps you plan your trips with personalized clothing recommendations based on real-time weather forecasts.**

This project is an assignment for the Software Development 2 course at Griffith College Cork Campus.

## 🌟 Project Overview

WeatherCare is a sophisticated chatbot application that combines AIML (Artificial Intelligence Markup Language) with real-time weather data to provide intelligent travel planning assistance. Users can input multiple destinations with specific dates, and the bot will provide detailed weather forecasts along with personalized clothing recommendations.

## 🚀 Key Features

### ✅ Implemented Features
- **AIML-powered Conversational AI**: Natural language processing using AIML patterns
- **Real-time Weather Integration**: OpenWeatherMap API integration for accurate 5-day forecasts
- **Multi-city Trip Planning**: Support for up to 5 destinations per query
- **Smart Clothing Recommendations**: Temperature-based suggestions for appropriate attire
- **Interactive Web Interface**: Modern, responsive UI with chat functionality
- **Multiple Communication Channels**: 
  - WebSocket for real-time chat
  - REST API for programmatic access
  - Web-based chat interface
- **Comprehensive Testing Suite**: Unit tests for core components
- **City Search Functionality**: Auto-complete city search feature
- **Date Validation**: Prevents past dates and limits to 5-day forecast window

## 🛠️ Technologies Used

### Backend Technologies
- **Java 11+**: Core backend language
- **Apache Tomcat 11.0.5**: Embedded web server and servlet container
- **Jakarta Servlet API**: Web application framework
- **Jakarta WebSocket API**: Real-time bidirectional communication
- **AIML (Artificial Intelligence Markup Language)**: Conversational AI patterns
- **Program AB**: AIML interpreter library
- **OpenWeatherMap API**: Weather data provider
- **JSON Processing**: Data parsing and manipulation
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework for tests

### Frontend Technologies
- **HTML5**: Modern web markup
- **CSS3**: Styling and responsive design
- **JavaScript (ES6+)**: Client-side functionality
- **jQuery 3.7.1**: DOM manipulation and AJAX
- **Bootstrap 4.1.3**: UI framework and components
- **Font Awesome 5.3.1**: Icon library
- **WebSocket Client**: Real-time communication
- **Boxicons**: Additional icon set

### Development Tools
- **Maven-style Library Management**: Organized dependency structure
- **Shell Scripts**: Automated build and run processes
- **Git**: Version control
- **Environment Variables**: Secure API key management

## 🏗️ Architecture

### System Components

1. **ServletMain**: Entry point that configures Tomcat server, sets up endpoints, and initializes WebSocket support
2. **ChatAPI**: REST API endpoint (`/chat/api`) for HTTP-based interactions
3. **ChatWebSocket**: WebSocket endpoint (`/chat`) for real-time communication
4. **ChatUI**: Servlet serving the web interface
5. **MessageHandler**: Core logic processor that handles AIML responses and weather queries
6. **WeatherData**: Weather API integration and data processing
7. **Frontend**: Interactive web interface with chat functionality

### Data Flow
1. User inputs travel plans through web interface
2. Frontend sends formatted message via WebSocket or REST API
3. MessageHandler processes the request using AIML patterns
4. Weather-related queries trigger WeatherData API calls
5. OpenWeatherMap API provides forecast data
6. System generates clothing recommendations based on temperature
7. Response is sent back to user with formatted weather and clothing advice

## 🌤️ Weather Features

- **5-Day Forecast Window**: Accurate predictions within API limitations
- **Temperature-based Recommendations**:
  - **Above 20°C**: Shorts and T-shirts recommended
  - **10-20°C**: Coat suggested for mild weather
  - **Below 10°C**: Coat strongly recommended for cold weather
- **Multiple City Support**: Plan trips across up to 5 different cities
- **Date Validation**: Prevents queries for past dates or beyond forecast range
- **Error Handling**: Graceful handling of invalid cities or dates

## 📋 Prerequisites

- **Java 11 or higher**
- **OpenWeatherMap API Key** (free registration at https://openweathermap.org/api)
- **Internet connection** for weather data
- **Modern web browser** with WebSocket support

## 🚀 Setup Instructions

### 1. Clone the Repository
```bash
git clone [repository-url]
cd jarvisbot
```

### 2. Get OpenWeatherMap API Key
1. Visit https://openweathermap.org/api
2. Sign up for a free account
3. Generate an API key
4. Save the API key (you'll need it in step 4)

### 3. Prepare the Environment
Ensure you have Java 11+ installed:
```bash
java -version
javac -version
```

### 4. Configure API Key
The application will prompt for your API key on first run, or you can create an `API_KEY.txt` file:
```bash
echo "your_openweathermap_api_key_here" > API_KEY.txt
```

### 5. Run the Application

#### On Linux/macOS:
```bash
chmod +x run.sh
./run.sh
```

#### On Windows:
```batch
run.bat
```

#### Manual Compilation and Run:
```bash
# Compile
javac -cp "./Backend/lib/*" ./Backend/src/*.java -d ./Backend/bin

# Set API key environment variable
export API_KEY="your_api_key_here"

# Run
java -cp "./Backend/bin:./Backend/lib/*" ServletMain
```

### 6. Access the Application
- **Web Interface**: http://localhost:8080
- **Chat Interface**: http://localhost:8080/chat
- **REST API**: http://localhost:8080/chat/api

## 🎯 Usage Examples

### Basic Weather Query
```
User: "I'm planning on going to Paris on 2025-07-15."
Bot: "Forecast for: Paris on Tuesday, July 15: 22.5°C. Warm day, sunny. You should wear shorts 🩳 and T-Shirts 👕"
```

### Multi-City Trip
```
User: "I'm planning on going to London on 2025-07-12, Paris on 2025-07-13, and Rome on 2025-07-14."
Bot: [Provides detailed forecast and clothing recommendations for each city and date]
```

## 🧪 Testing

Run the test suite:
```bash
java -cp "./Backend/bin:./Backend/lib/*" org.junit.platform.console.ConsoleLauncher --class-path "./Backend/bin" --scan-class-path
```

### Test Coverage
- ✅ MessageHandler functionality
- ✅ WeatherData API integration
- ✅ ChatAPI REST endpoints

## 📁 Project Structure

```
jarvisbot/
├── Backend/
│   ├── src/           # Java source files
│   ├── bin/           # Compiled classes
│   ├── lib/           # JAR dependencies
│   └── ab/            # AIML bot configuration
│       └── bots/jarvis/
│           ├── aiml/  # AIML pattern files
│           ├── aimlif/# Learned responses
│           └── sets/  # Bot knowledge sets
├── Frontend/
│   ├── index.html     # Main web interface
│   ├── chat.js        # Chat functionality
│   ├── citiesSearch.js# City autocomplete
│   └── css/           # Stylesheets
├── run.sh             # Linux/macOS startup script
├── run.bat            # Windows startup script
└── API_KEY.txt        # Weather API key (created on first run)
```

## 🔧 Configuration

### AIML Patterns
The bot's conversational patterns are defined in `/Backend/ab/bots/jarvis/aiml/` files:
- `weather.aiml`: Weather-related conversation patterns
- Additional pattern files for extended functionality

### API Configuration
- Weather API: OpenWeatherMap (configurable in WeatherData.java)
- Server Port: 8080 (configurable in ServletMain.java)
- Forecast Limit: 5 days (API limitation)

## 🤝 Contributing

This is an academic project for Griffith College Cork Campus. For educational purposes and collaboration:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is created for educational purposes as part of the Software Development 2 course at Griffith College Cork Campus.

## 🆘 Troubleshooting

### Common Issues

**Server won't start:**
- Ensure Java 11+ is installed
- Check if port 8080 is available
- Verify API key is properly set

**Weather data not loading:**
- Verify OpenWeatherMap API key is valid
- Check internet connection
- Ensure API key has sufficient quota

**Chat not connecting:**
- Refresh the webpage
- Check browser console for WebSocket errors
- Verify server is running on localhost:8080

*WeatherCare - Your climate in good hands* 🌤️


