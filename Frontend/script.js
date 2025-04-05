// Use jQuery to select elements
var messageInput;
const sendButton = $('.send-btn');
const chatMessages = $('.chat-messages');
const planningTrigger = $('#planningTrigger');

planningTrigger.on('click', () => {
    $('#weatherSearch').show();
    planningTrigger.hide();
    messageInput = "Hi, I'm planning a trip. Can you help me plan my clothing requirements?"
    sendMessage(messageInput);
})

/// Websocket setup
const ws = new WebSocket('ws://localhost:8080/chat');

ws.onopen = () => {
    console.log('Connected to WebSocket server');
    addMessage('Connected to the server', 'system');
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

// Send message to WS server
function sendMessage(text) {
    addMessage(text, 'user');
    ws.send(text);
}

// Function to add messages to the UI
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

