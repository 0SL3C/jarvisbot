import React from 'react';
import Message from './Message'

const ChatWindow = ({messages}) => {
    return (
        <div className = "bg-white round-lg shadow-md p-4 mb-4 h-96 overflow-y-auto">
            {messages.map((message, index) =>(
            <Message
            key={index}
            text={message.text}
            isBot={message.isBot}
            />
            ))}
            <div className="debug-border h-64">  {/* Altura fixa para visualização */}
  {/* ... conteúdo existente ... */}
            </div>
        </div>
        
        )
        
    }

    export default ChatWindow