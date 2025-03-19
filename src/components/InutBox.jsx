import {useState} from 'react'
import {FiSend} from 'react-icons/fi'

const InputBox = ({ onSend }) => {
    const [message, setMessage] = useState('')

    const handleSubmit = (e) => {
            e.preventDefault()
            if (message.trim()) {
                onSend(message)
                setMessage('')
            }
        }

        return (
            <form onSubmit={handleSubmit} className="flex gap-2">
                <input
                 type="text"
                 value={message}
                 onChange={(e) => setMessage(e.target.value)}
                 placeholder="type your destination and dates"
                 className="flex-1 p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
                
                <button
                type="submit" className="bg-blue-500 text-white p-2 rounded-lg hover:bg-blue-600 transition-colors">
                    <FiSend size={20} />
                </button>
            </form>
            )
        }

        export default InputBox