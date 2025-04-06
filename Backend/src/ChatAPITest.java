import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;

public class ChatAPITest {
    private ChatAPI chatAPI;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        chatAPI = new ChatAPI();
        chatAPI.init(); // Initialize the bot and chat

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoPost() throws Exception {
        // Mock the request body
        String userMessage = "Hello, bot!";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(userMessage)));

        // Call the doPost method
        chatAPI.doPost(request, response);

        // Verify the response
        String botResponse = responseWriter.toString();
        assertNotNull(botResponse);
        assertFalse(botResponse.isEmpty());
    }
}