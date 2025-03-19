import { useState } from 'react';
import ChatWindow from '../components/ChatWindow';
import InputBox from '../components/InputBox';

export default function Home() {
  const [messages, setMessages] = useState([]);

  const handleSend = (newMessage) => {
    setMessages([...messages, { text: newMessage, isBot: false }]);

    setTimeout(() => {
      setMessages(prev => [...prev, { text: 'Processing your query...', isBot: true }]);
    }, 1000);
  };

  return (
    <div className="max-w-2xl mx-auto p-4">
      <h1 className="text-3xl font-bold text-center mb-8 text-blue-600">
        Travel Outfit Planner
      </h1>

      <ChatWindow messages={messages} />

      <InputBox onSend={handleSend} />
    </div>
  );
}