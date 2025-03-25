import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.alicebot.ab.*;
import java.io.IOException;

@ServerEndpoint("/chat")
public class ChatWebSocket{
    private Bot bot;
    private Chat chat;

    @OnOpen
    public void onOpen(Session session){
        System.out.println("A new websocket has been established. ID: " + session.getId());
        bot = new Bot("jarvis", "Backend/ab", "chat");
        chat = new Chat(bot);
        try{
            session.getBasicRemote().sendText("Hello, how can I help you today?");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException{
        System.out.println("Message received: " + message);
        String botResponse = chat.multisentenceRespond(message);
        session.getBasicRemote().sendText(botResponse);
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("A websocket has been closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        System.err.println("Websocket error: " + throwable.getMessage());
    }
}