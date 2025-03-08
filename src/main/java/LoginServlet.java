import org.example.CustomerLogin;
import org.example.SQLQuery;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("email: " + email + " password: " + password);
        SQLQuery sql = new SQLQuery();
        if (sql == null) {
            return ;
        }
        CustomerLogin login = new CustomerLogin(sql);
        if (login.login(email, password)) {
            request.getSession().setAttribute("user", email);
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
