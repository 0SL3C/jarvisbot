import org.alicebot.ab.*;

public class BotManager {
    private static BotManager instance;
    private Bot botInstance;
    private Chat chatSession;
    
    /// BotManager constructor
    private BotManager(){
    }

    /// Static method that return bot based on its name
    public static BotManager getManagerInstance() {
        if (instance == null) {
            instance = new BotManager(); // If Bot is null return a new Bot;
        }
        return instance;
    }


    /// Method to create bot
    public Bot createBot(String botName){
        this.botInstance = new Bot(botName, "Backend/ab", "chat");
        chatSession = new Chat(botInstance);
        return botInstance;
    }

    /// Standard getInstance that has parameters
    public Bot getInstance(String botName) {
        if (botInstance == null) {
            botInstance = this.createBot(botName);
        }
        return botInstance;
    }
    
    /// Standard getInstance that uses default values
    public Bot getInstance() {
        if (botInstance == null) {
            botInstance = this.createBot("jarvis");
        }
        return botInstance;
    }

    /// Shutdown bot
    public void shutdown(String botName){
        if (this.getInstance(botName) != null){
            this.getInstance(botName).writeQuit();
        }
    }
    /// Shutdown without parameters
    public void shutdown(){
        if (this.botInstance != null){
            this.botInstance.writeQuit();
        }
    }

    /// ChatSession GET method
    public Chat getChatSession(){
        return this.chatSession;
    }
}
