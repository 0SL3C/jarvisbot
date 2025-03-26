

// ===== CONSTANTES =====
const DOM = {
    messageInput: document.querySelector('.message-input'),
    sendButton: document.querySelector('.send-btn'),
    chatMessages: document.querySelector('.chat-messages'),
    themeToggle: document.querySelector('.theme-toggle')
};

// ===== FUNÇÃO ADICIONADA =====
function createTypingIndicator() {
    const typingDiv = document.createElement('div');
    typingDiv.className = 'message bot-message typing-indicator';
    typingDiv.innerHTML = `
        <div style="display: flex; gap: 0.5rem;">
            <div class="dot" style="animation-delay: 0s"></div>
            <div class="dot" style="animation-delay: 0.2s"></div>
            <div class="dot" style="animation-delay: 0.4s"></div>
        </div>
    `;
    return typingDiv;
}


// ===== MANIPULAÇÃO DE TEMA =====
function toggleTheme() {
    const currentTheme = document.body.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    
    document.body.setAttribute('data-theme', newTheme);
    DOM.themeToggle.innerHTML = newTheme === 'dark' 
        ? '<i class="bx bx-moon"></i>' 
        : '<i class="bx bx-sun"></i>';
}

// ===== MANIPULAÇÃO DE MENSAGENS =====
function createMessageElement(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}-message`;
    
    messageDiv.innerHTML = `
        <p>${text}</p>
        <span class="timestamp">${new Date().toLocaleTimeString([], { 
            hour: '2-digit', 
            minute: '2-digit' 
        })}</span>
    `;
    
    return messageDiv;
}

async function getWeatherData(city) {
    try {
        const response = await fetch('http://localhost:3000/weather', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ city })
        });
        
        return await response.json();
    } catch (error) {
        console.error('Erro na requisição:', error);
        return { error: "Falha na conexão com o servidor" };
    }
}

// ===== HANDLERS PRINCIPAIS =====
async function handleMessageSubmission() {
    const userMessage = DOM.messageInput.value.trim();
    if (!userMessage) return;

    // Adiciona mensagem do usuário
    DOM.chatMessages.appendChild(createMessageElement(userMessage, 'user'));
    DOM.messageInput.value = '';

    try {
        // Mostra indicador de digitação
        const typingIndicator = createTypingIndicator();
        DOM.chatMessages.appendChild(typingIndicator);
        
        // Simula resposta
        const botResponse = await getWeatherData(userMessage);
        typingIndicator.remove();
        DOM.chatMessages.appendChild(createMessageElement(botResponse, 'bot'));
        
        // Rolagem automática
        DOM.chatMessages.scrollTop = DOM.chatMessages.scrollHeight;
    } catch (error) {
        DOM.chatMessages.appendChild(createMessageElement("Connection error. Please try again.", 'bot'));
    }
}

// ===== INICIALIZAÇÃO =====
function initEventListeners() {
    // Envio de mensagem
    DOM.sendButton.addEventListener('click', handleMessageSubmission);
    DOM.messageInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleMessageSubmission();
    });

    // Toggle de tema
    DOM.themeToggle.addEventListener('click', toggleTheme);

    // Microinteração no input
    DOM.messageInput.addEventListener('input', (e) => {
        DOM.sendButton.style.transform = e.target.value.length > 0 
            ? 'scale(1.1)' 
            : 'scale(1)';
    });
}

// Inicia a aplicação
document.addEventListener('DOMContentLoaded', initEventListeners);