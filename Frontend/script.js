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