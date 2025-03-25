import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class ServletMain {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8080);
        tomcat.setConnector(connector);

        /// Create a File Handler pointing to the ./Frontend folder
        String webappDir = new File("Frontend").getAbsolutePath();
        Context ctx = tomcat.addContext("", webappDir);

        // Add the servlet and map it to "/chat"
        Tomcat.addServlet(ctx, "ChatServlet", "ChatServlet");
        ctx.addServletMappingDecoded("/chat", "ChatServlet");
        
        tomcat.start();
        System.out.println("Tomcat server started on port 8080");
        tomcat.getServer().await();
    }
}