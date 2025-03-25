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
        System.out.println("\n" + "#".repeat(24));
        System.out.println("NEW WEBSOCKET ESTABLISHED. ID: " + session.getId());
        System.out.println("#".repeat(24) + "\n");
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