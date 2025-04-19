let ws;

$(document).ready(() => {
    console.log('chat.js loaded');
    $('#weatherOptions').hide();

    // Function to set min and max dates for date inputs
    function setDateConstraints() {
        const today = new Date();
        const maxDate = new Date(today);
        maxDate.setDate(today.getDate() + 5); // Today + 5 days

        // Format dates as YYYY-MM-DD
        const formatDate = (date) => {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        };

        // Set min and max for all date inputs
        $('.date-input').attr({
            'min': formatDate(today),
            'max': formatDate(maxDate)
        });
    }

    // Call initially to set constraints
    setDateConstraints();

    // Toggle chatbox visibility and manage WebSocket connection
    $('#chatToggle').on('click', () => {
        console.log('Chat toggle clicked');
        $('#chatbox').toggleClass('hidden');
        if ($('chatbox').hasClass('hidden')) {
            // If chatbox is hidden, close the WebSocket connection
            if (ws && ws.readyState === WebSocket.OPEN) {
                ws.close();
            }
        } else {
            // If chatbox is visible, create a new WebSocket connection
            if (!ws || ws.readyState === WebSocket.CLOSED) {
                ws = connectWebSocket();
            }
        }
    });

    // Close chat
    $('#closeChat').on('click', () => {
        $('#chatbox').addClass('hidden');
        ws.close();
    });

    // Weather button
    $('#weatherBtn').on('click', () => {
        $('#weatherBtn').hide();
        $('#weatherOptions').show();
    });

    let count = 1;
    
    $('#addBtn').off('click').on('click', () => {
        // Increment the count and hide the add button if the limit is reached
        count++;
        if (count === 5) {
            $('#addBtn').hide();
        }
    
        // Clone the input row template
        const newInputRow = $('.input-row:first').clone();
    
        // Clear the values of the cloned inputs
        newInputRow.find('.text-input').val('');
        newInputRow.find('.date-input').val('');
    
        // Append the cloned row to the #inputs container
        $('#inputs').append(newInputRow);
    });

    // Send button click handler
    $('#sendBtn').on('click', function () {
        // Collect all input rows from the #inputs div
        const inputRows = $('#inputs .input-row');
        const tripPlans = [];
    
        // Iterate over each input row to get city and date values
        inputRows.each(function() {
            const city = $(this).find('.text-input').val().trim();
            const date = $(this).find('.date-input').val().trim();
            // Only include pairs where both city and date are non-empty
            if (city && date) {
                tripPlans.push({ city, date });
            }
        });
    
        // If there are valid pairs, construct and send the message
        if (tripPlans.length > 0) {
            // Format WebSocket message: weather:city:date,city:date,...
            const wsMessage = `weather:${tripPlans.map(plan => `${plan.city}:${plan.date}`).join(',')}`;
    
            // Format chatbox message
            let chatMessage;
            if (tripPlans.length === 1) {
                // Single destination: "I'm planning on going to Paris on 2023-05-01."
                chatMessage = `I'm planning on going to ${tripPlans[0].city} on ${tripPlans[0].date}.`;
            } else if (tripPlans.length === 2) {
                // Two destinations: "I'm planning on going to Paris on 2023-05-01 and London on 2023-05-10."
                chatMessage = `I'm planning on going to ${tripPlans[0].city} on ${tripPlans[0].date} and ${tripPlans[1].city} on ${tripPlans[1].date}.`;
            } else {
                // Three or more: "I'm planning on going to Paris on 2023-05-01, London on 2023-05-10, and New York on 2023-05-20."
                const last = `${tripPlans[tripPlans.length - 1].city} on ${tripPlans[tripPlans.length - 1].date}`;
                const firstPart = tripPlans.slice(0, -1).map(plan => `${plan.city} on ${plan.date}`).join(', ');
                chatMessage = `I'm planning on going to ${firstPart}, and ${last}.`;
            }
    
            // Send the WebSocket message and add the chatbox message
            sendMessage(ws, wsMessage);
            addMessage(chatMessage, 'user');
        } else {
            // Log a message if no valid pairs are found
            console.log('No valid city and date entered.');
        }
    });
});

// Function to establish WebSocket connection
function connectWebSocket() {
    const ws = new WebSocket('ws://localhost:8080/chat');

    ws.onopen = () => {
        console.log('Connection to WebSocket established');
    };

    ws.onmessage = (event) => {
        console.log('Message received from server:', event.data);
        addMessage(event.data, 'bot');
    };

    ws.onerror = (error) => {
        console.error('WebSocket error:', error);
    };

    ws.onclose = () => {
        console.log('WebSocket connection closed');
        $('#chat-messages').empty();
        $('#weatherBtn').show();
        $('#weatherOptions').hide();
    };

    return ws;
}

// Function to send a message through WebSocket
function sendMessage(ws, message) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(message);
        console.log('Message sent:', message);
    } else {
        console.log('WebSocket is not open. Cannot send message.');
    }
}

// Function to add a message to the chatbox
function addMessage(text, sender) {
    // Create a new <li> element for the message
    const messageItem = $('<li></li>'); // Use jQuery to create the element

    // Add a class based on the sender (e.g., 'bot-message' or 'user-message')
    messageItem.addClass(`${sender}-message`);

    // Format the message content
    const messageContent = `
        <div class="message-content">
            <p>${text.replace(/\n/g, '<br>')}</p>
            <small class="timestamp">${new Date().toLocaleTimeString()}</small>
        </div>
    `;

    // Append the content to the <li> element
    messageItem.html(messageContent);

    // Append the <li> to the chat messages list
    $('#chat-messages').append(messageItem);

    // Scroll to the bottom of the chat messages container
    const messagesContainer = $('#chat-messages')[0];
    messagesContainer.scrollTo({
        top: messagesContainer.scrollHeight,
        behavior: 'smooth',
    });
}