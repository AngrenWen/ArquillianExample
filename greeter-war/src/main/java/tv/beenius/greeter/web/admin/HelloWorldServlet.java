package tv.beenius.greeter.web.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import app.deployment.admin.servlet.SkeletonServlet;

public class HelloWorldServlet extends SkeletonServlet{

	private static final long serialVersionUID = -4448903719374394429L;
    private static final Logger logger = Logger.getLogger(HelloWorldServlet.class.getName());

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       super.doGet(request, response);

       RequestDispatcher dispatcher = null;

       dispatcher = request.getRequestDispatcher("/admin/helloWorld.jsp");
       if (dispatcher != null)
       {
          dispatcher.forward(request, response);
          return;
       }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
       doGet(request, response);
    }
    
    
}
