import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import org.alicebot.ab.*;

@WebServlet
public class ChatUI extends HttpServlet{
    private Bot bot;
    private Chat chat;

    /// Servlet initialization
    @Override
    public void init() throws ServletException {
        /// Creates bot
        bot = new Bot("jarvis", "Backend/ab", "chat");
        /// Chat handler with bot
        chat = new Chat(bot);
    }

    /// GET Method
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // Get the subpath after
        if (pathInfo == null || pathInfo.equals("/")) {
            // Serve index.html for /chat or /chat/
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            String htmlContent = getStaticFileContent("index.html");
            if (htmlContent != null) {
                response.getWriter().write(htmlContent);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "index.html not found");
            }
        } else {
            // Serve other static files (e.g., /chat/script.js, /chat/style.css)
            String fileName = pathInfo.substring(1); // Remove leading "/"
            String contentType = getContentType(fileName);
            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");

            String fileContent = getStaticFileContent(fileName);
            if (fileContent != null) {
                response.getWriter().write(fileContent);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, fileName + " not found");
            }
        }
    }

    // Method to read static file content from Frontend/
    private String getStaticFileContent(String fileName) throws IOException {
        File file = new File("Frontend/" + fileName);
        if (file.exists()) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }
        return null;
    }

    // Method to determine content type based on file extension
    private String getContentType(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "text/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            return "application/octet-stream"; // Default for unknown types
        }
    }
}
