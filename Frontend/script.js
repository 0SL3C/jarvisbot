// Use jQuery to select elements
const messageInput = $('#cityInput'); 
const sendButton = $('.send-btn');
const chatMessages = $('.chat-messages');

// Websocket setup
const ws = new WebSocket('ws://localhost:8080/chat');

// Função para adicionar mensagens formatadas
function addMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message mb-3`;
    
    // Formatação diferente para bot e user
    if(sender === 'bot') {
        messageDiv.innerHTML = `
            <div class="card bg-dark text-white border-0">
                <div class="card-body p-3">
                    <p class="mb-0">${text.replace(/\n/g, '<br>')}</p>
                    <small class="text-muted">${new Date().toLocaleTimeString()}</small>
                </div>
            </div>
        `;
    } else {
        messageDiv.innerHTML = `
            <div class="card bg-primary text-white border-0">
                <div class="card-body p-3">
                    <p class="mb-0">${text}</p>
                    <small class="text-white-50">${new Date().toLocaleTimeString()}</small>
                </div>
            </div>
        `;
    }

    chatMessages.append(messageDiv);
    const messagesContainer = $('.chat-messages')[0];
    messagesContainer.scrollTo({
        top: messagesContainer.scrollHeight,
        behavior: 'smooth'
    });
}

// WebSocket handlers
ws.onopen = () => {
    console.log('Connected to WebSocket server');
    addMessage('Connected to travel assistant', 'bot');
}

ws.onmessage = (event) => {
    addMessage(event.data, 'bot');
}

ws.onerror = (error) => {
    addMessage('Connection error: ' + error.message, 'system');
}

ws.onclose = () => {
    addMessage('Disconnected from server', 'system');
}

// Send message function
function sendMessage() {
    const userMessage = messageInput.val().trim();
    if (!userMessage) return;

    addMessage(userMessage, 'user');
    ws.send(userMessage);
    messageInput.val('');
}

sendButton.on('click', () => {
    console.log("Button triggered");
    sendMessage();
});

messageInput.on('keypress', (e) => {
    if (e.key === 'Enter') sendMessage();
});

// Automatic focus on Input field
$(document).ready(function() {
    messageInput.focus();
});

document.addEventListener("DOMContentLoaded", () => {
    const cityContainer = document.getElementById("cityContainer");
    let cityCount = 1; // Cities accountant
  
    cityContainer.addEventListener("click", function (e) {
      // Checks if the Add City button has been clicked
      if (e.target.closest(".add-city-btn")) {
        const addButton = e.target.closest(".add-city-btn");
  
        if (cityCount >= 5) {
            alert("You can only add up to 5 cities.");
            return;
        }

        // Hide the current button
        addButton.style.display = "none";
  
        // Creates the new city field
        const newRow = document.createElement("div");
        newRow.classList.add("city-row");
  
        newRow.innerHTML = `
          <div class="search-container d-flex align-items-center gap-2 mt-2">
            <input type="text" class="message-input" placeholder="Search for a city...">
            <input type="date" class="date-input">
            <button class="add-city-btn btn btn-outline-light">
              <i class='bx bx-plus'></i>
            </button>
          </div>
        `;
  
        // Adds the new field to the interface
        cityContainer.appendChild(newRow);

        cityCount++; // Increments the city count
  
        // Makes the new button visible
        const newButton = newRow.querySelector(".add-city-btn");
        newButton.style.display = "block";
      }
    });
  })