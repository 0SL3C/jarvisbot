// Use jQuery to select elements
const messageInput = $('#cityInput'); 
const sendButton = $('.send-btn');
const chatMessages = $('.chat-messages');

// Websocket setup
const ws = new WebSocket('ws://localhost:8080/chat');

// Function to add formatted messages
function addMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message mb-3`;
    
    // Different formatting for bot and user
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
    const rows = document.querySelectorAll(".city-row");
    let parts = [];

    rows.forEach(row => {
        const cityInput = row.querySelector(".message-input");
        const dateInput = row.querySelector(".date-input");

        const city = cityInput?.value?.trim();
        const date = dateInput?.value?.trim();

        if(city && date){
            const day = new Date(date).toLocaleDateString('en-US', { weekday: 'long' }).toLowerCase();
            parts.push(`${city.toLowerCase()}:${day}`);
        }
    });

    if (parts.length > 0 ){
        const fullMessge = `weather:${parts.join(",")}`;

        addMessage(fullMessge, 'user'); // Display the message in the chat
        ws.send(fullMessge);// Send the message to the server
    } else {
        alert("Please fill in all fields before sending.");
    }
});

messageInput.on('keypress', (e) => {
    if (e.key === 'Enter') sendMessage();
});

// Automatic focus on Input field
$(document).ready(function() {
    messageInput.focus();
});

document.addEventListener("DOMContentLoaded", () => {

    messageInput.focus(); // Focus on the input field when the page loads



    const cityContainer = document.getElementById("cityContainer");
    const today = new Date().toISOString().split('T')[0];

    // Sets the minimum date to today and future days
    const applyMinDate = () => {
        document.querySelectorAll(".date-input").forEach(input => {
            input.setAttribute("min", today);
        });
    };

    applyMinDate(); // Apply the minimum date on load

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

        applyMinDate(); // Apply the minimum date to the new input field
      }
    });
        // -------------------------
        // 🌤️ SIDEBAR 
        // -------------------------
        const sidebar = document.getElementById("weatherSidebar");
        const toggleBtn = document.getElementById("toggleSidebarBtn");;
        if (toggleBtn && sidebar) {
            toggleBtn.addEventListener("click", () => {
              sidebar.classList.toggle("open");
              console.log("🔄 Sidebar toggled");
            });
          } else {
            console.log("❌ Sidebar or button not found");
          }
        });

        //adds event to capture clicks in Bot messages
        $(document).on("click", ".bot-message .card", function () {
    const messageText = $(this).find("p").html();
    
    // Extrais City and Date with Simple Regex
    const match = messageText.match(/([A-Za-z\s]+) on ([A-Za-z]+), ([A-Za-z]+ \d+)/);
    if (!match) return;

    const city = match[1].trim();
    const weekday = match[2];
    const fullDateText = match[3];

    const sidebar = document.getElementById("weatherSidebar");
    const sidebarContent = document.getElementById("sidebarContent");

    
    sidebarContent.innerHTML = `
        <h5>${city}</h5>    
        <p><strong>Day:</strong> ${weekday}, ${fullDateText}</p>
        <p><strong>Weather:</strong> ${messageText.split('<br>')[1]}</p>
        <p><strong>Clothing Advice:</strong> ${messageText.split('<br>')[2]}</p>
    `;

    sidebar.classList.add("open");
});