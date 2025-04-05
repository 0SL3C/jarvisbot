// Use jQuery to select elements
const messageInput = $('.message-input');
const sendButton = $('.send-btn');
const chatMessages = $('.chat-messages');

/// Websocket setup
const ws = new WebSocket('ws://localhost:8080/chat');
ws.onopen = () => {
    console.log('Connected to WebSocket server');
}
ws.onmessage = () => {
    const botMessage = event.data;
    addMessage(botMessage, 'bot');
}
ws.onerror = (error) => {
    addMessage('Connection error', 'system');
}
ws.onclose = () => {
    addMessage('Disconnected from server', 'system');
}

// Function to add messages
function addMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;
    messageDiv.innerHTML = `
        <p>${text}</p>
        <span class="timestamp">${new Date().toLocaleTimeString()}</span>
    `;

    chatMessages[0].appendChild(messageDiv); // Use [0] since jQuery returns a collection
    chatMessages[0].scrollTop = chatMessages[0].scrollHeight;
}

// // 2. simultion of messages
// async function mockBackendResponse(userMessage) {
//     await new Promise(resolve => setTimeout(resolve, 1000));
    
//     const responses = {
//         "hi": "Hello! where you want to travel? 🌍",
//         "Nova York": "Nova York It's 15°C and cloudy. I recommend a light coat!",
//         "default": "Think about your neessity... ✈️"
//     };
    
//     return responses[userMessage.toLowerCase()] || responses.default;
// }

// // 3. indicator of typing
// function showTypingIndicator() {
//     const typingDiv = document.createElement('div');
//     typingDiv.className = 'typing-indicator';
//     typingDiv.innerHTML = `
//         <div class="dot"></div>
//         <div class="dot"></div>
//         <div class="dot"></div>
//     `;
//     chatMessages.appendChild(typingDiv);
//     return typingDiv;
// }

// Send message via WebSocket
function sendMessage() {
    const userMessage = messageInput.val().trim(); // Use .val() for jQuery input value
    if (!userMessage) return;

    addMessage(userMessage, 'user');
    ws.send(userMessage);
    messageInput.val(''); // Clear input using jQuery
}

// // 4. Handler principal
// async function handleUserMessage() {
//     const userMessage = messageInput.value.trim();
//     if (!userMessage) return;

//     addMessage(userMessage, 'user');
//     messageInput.value = '';

//     try {
//         const typingIndicator = showTypingIndicator();
//         const botResponse = await mockBackendResponse(userMessage);
//         typingIndicator.remove();
//         addMessage(botResponse, 'bot');
//     } catch (error) {
//         addMessage("Erro de conexão com o servidor", 'system');
//     }
// }

// Event listeners
sendButton.on('click', sendMessage); // Use jQuery .on() instead of addEventListener
sendButton.on('click', console.log("Button triggered"));
messageInput.on('keypress', (e) => {
    if (e.key === 'Enter') sendMessage() && console.log("Enter triggered");
});


//weather painel
// Render 5-day forecast cards
function renderForecastCards(forecastData) {
    const forecastContainer = $('.forecast-container');
    forecastContainer.html(''); // Clear existing content using jQuery

    forecastData.forEach(day => {
        const card = $('<div>').addClass('forecast-card');
        card.html(`
            <h3>${day.date}</h3>
            <p>Temperature: ${day.temperature}°C</p>
            <p>Condition: ${day.condition}</p>
        `);
        forecastContainer.append(card);
    });
}

// Sample forecast data
const sampleForecastData = [
    { date: '2023-10-01', temperature: 20, condition: 'Sunny' },
    { date: '2023-10-02', temperature: 18, condition: 'Cloudy' },
    { date: '2023-10-03', temperature: 22, condition: 'Rainy' },
    { date: '2023-10-04', temperature: 19, condition: 'Windy' },
    { date: '2023-10-05', temperature: 21, condition: 'Sunny' }
];

// Function to fetch weather forecast data from server
function fetchWeatherForecast(location) {
    // This would normally be an API call to the server
    // For now, we'll just return the sample data
    return new Promise(resolve => {
        setTimeout(() => {
            resolve(sampleForecastData);
        }, 500);
    });
}

// Function to update weather forecast when a location is mentioned
function updateWeatherForecast(location) {
    fetchWeatherForecast(location)
        .then(data => {
            renderForecastCards(data);
            $('.forecast-section').show();
        })
        .catch(error => {
            console.error('Error fetching weather data:', error);
        });
}

// Initialize forecast section (hidden by default)
$(document).ready(function() {
    // Add forecast container if it doesn't exist
    if ($('.forecast-container').length === 0) {
        $('body').append(`
            <div class="forecast-section">
                <h2>Weather Forecast</h2>
                <div class="forecast-container"></div>
            </div>
        `);
        $('.forecast-section').hide();
    }
    
    // Modify the WebSocket message handler to check for location mentions
    ws.onmessage = (event) => {
        const botMessage = event.data;
        addMessage(botMessage, 'bot');
        
        // Check if message contains location information
        if (botMessage.includes('°C') && botMessage.includes('recommend')) {
            // Extract location from previous user message
            const lastUserMessage = $('.user-message p').last().text();
            updateWeatherForecast(lastUserMessage);
        }
    };
});