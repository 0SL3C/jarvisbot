import React from 'react';
import Message from './Message'

const ChatWindow = ({messages}) => {
    return (
        <div className = "bg-white round-lg shadow-md p-4 mb-4 h-96 overflow-y-auto">
            {messages.map((message, index) =>(
            <Message
            Key={index}
            text={message.text}
            isBot={messages.isBot}
            />
            ))}
        </div>
        )
    }

    export default ChatWindow