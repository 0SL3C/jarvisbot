import { FaUser, FaRobot} from 'react-icons/fa' //Fontes used from the Fontawesome website to use on react

const Message = ({ text, isBot }) => { // commonly used to display messages on a chat
 return (
    <div className={`flex items-center gap-2 mb-1 ${isBot ? 'justify-start' : 'justify-end'}`}> //Bootstrap Command for FlexBox Responsives
        <div className={`p-3 rounded-lg max-w-xs lg:max-w-md ${isBot ? 'bg-gray-100' : 'bg-blue-100'}`}>
            <div className="flex items-center gap-2 mb-1">
                {isBot ? (
                    <FaRobot className="text-gray-600"/>
                ) : (
                    <FaUser className="text-blue-600"/>
                )}
                <span className="font-medium">
                    {isBot ? 'Trvel Bot' : 'You'}
                </span>
            </div>
            <p className="text-gray-800">{text}</p>
        </div>
    </div>
            )
        }

        export default Message