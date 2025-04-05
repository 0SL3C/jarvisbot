// Use jQuery to select elements
const messageInput = $('.message-input');
const sendButton = $('.send-btn');
const chatMessages = $('.chat-messages');
const planningTrigger = $('#planningTrigger');

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


// Send message via WebSocket
function sendMessage() {
    const userMessage = messageInput.val().trim(); // Use .val() for jQuery input value
    if (!userMessage) return;

    addMessage(userMessage, 'user');
    ws.send(userMessage);
    messageInput.val(''); // Clear input using jQuery
}

// Event listeners
sendButton.on('click', sendMessage); // Use jQuery .on() instead of addEventListener
sendButton.on('click', console.log("Button triggered"));
messageInput.on('keypress', (e) => {
    if (e.key === 'Enter') sendMessage() && console.log("Enter triggered");
});

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