    import { useState} from 'react'
    import ChatWindow from '../components/ChatWindow'
    import InputBox from '../components/InputBox'

    const Home = () => {
        const [ messages, setMessages] = useState([]) //useState([]) inicializa a menssagem com array vazio e armazena as mensagems
        
    const handleSend = (newMessage) => {
        setMessages([...messages, { text: newMessage, isBot: false}])

        setTimeout(() =>{
            setMessages(prev => [...prev, {text: 'processing you query...', isBot: true}])
        },
             1000)

    }

    return (
        <div className="max-w-2x1 mx-auto p-4">
            <h1 className="text-3x1 font-bolt text-center mb-8 text-blue-600">
                Travel outfit Planner
            </h1>

            <ChatWindow messages={messages}/>

            <InputBox onSend={handleSend}/>

        </div>
    )
}