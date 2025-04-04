import static org.junit.jupiter.api.Assertions.*;

import org.alicebot.ab.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageHandlerTest {
    private Bot bot;
    private Chat chat;
    MessageHandler messageHandler; 

    @BeforeEach
    void setup(){
        bot = new Bot("jarvis", "Backend/ab", "chat");
        bot.writeAIMLIFFiles();
        chat = new Chat(bot);
        messageHandler = new MessageHandler(chat);
    }

    @Test
    void testProcessMessage() {
        String input = "I will be visiting London on Monday";
        String result = messageHandler.processMessage(input);
        assertNull(result, result);
    }
}
