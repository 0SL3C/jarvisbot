import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import org.alicebot.ab.*;

// Initialize WebServlet using /chat page
@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private Bot bot;
    private Chat chat;

    /// Server initialization
    @Override
    public void init() throws ServletException {
        /// Creates bot
        bot = new Bot("jarvis", "Backend/ab", "chat");
        /// Chat handler with bot
        chat = new Chat(bot);
    }

    /// POST method
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /// Read request body
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        /// Parse JSON request
        String requestBody = buffer.toString();
        String responseMessage = "Received message: " + requestBody;

        /// Send response back as plain text
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseMessage);
    }

    /// GET Method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Set response content type to HTML
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // Serve index.html content (embedded or read from file)
        String htmlContent = getIndexHtmlContent();
        response.getWriter().write(htmlContent);
    }

    /// Method to get index.html content
    private String getIndexHtmlContent() throws IOException {
        /// Read from file
        File htmlFile = new File("Frontend/index.html");
        if (htmlFile.exists()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(htmlFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n"); /// For each line, add line and line break, next line...
                }
            }
            return content.toString();
        }
        return null;
    }
}