import org.example.CustomerLogin;
import org.example.SQLQuery;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        SQLQuery sql = new SQLQuery();
        CustomerLogin login = new CustomerLogin(sql);
        if (login.register(name, email, password)) {
            request.getSession().setAttribute("user", email);
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
