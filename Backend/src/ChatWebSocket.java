import java.io.IOException;
import org.alicebot.ab.*;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatWebSocket{

    private Bot bot;
    private Chat chat;
    private MessageHandler msgHND;

    //////////////////////////////////////////////////////// WEB SOCKET HANDLING ////////////////////////////////////////////////////////
    @OnOpen
    public void onOpen(Session session){
        System.out.println("\n" + "#".repeat(24));
        System.out.println("NEW WEBSOCKET ESTABLISHED. ID: " + session.getId());
        System.out.println("#".repeat(24) + "\n");
        bot = new Bot("jarvis", "Backend/ab", "chat");
        bot.writeAIMLIFFiles();
        chat = new Chat(bot);
        try{
            session.getBasicRemote().sendText("Hello, how can I help you today?");
            msgHND = new MessageHandler(chat);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("Message received: " + message);
        String result = msgHND.processMessage(message);
        session.getBasicRemote().sendText(result);
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("A websocket has been closed: " + session.getId());
    }
    @OnError
    public void onError(Session session, Throwable throwable){
        System.err.println("Websocket error: " + throwable.getMessage());
    }

//////////////////////////////////////////////////////// END WEB SOCKET HANDLING ////////////////////////////////////////////////////////
}