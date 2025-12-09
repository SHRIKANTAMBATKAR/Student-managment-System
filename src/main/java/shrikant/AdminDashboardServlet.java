package shrikant;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        response.setContentType("text/html");
        response.getWriter().println("<h1>Welcome Admin, " + session.getAttribute("user") + "</h1>");
        response.getWriter().println("<a href='view_students.html' class='btn btn-primary'>View All Students</a><br>");
        response.getWriter().println("<a href='register_student.html' class='btn btn-success mt-2'>Register New Student</a><br>");
        response.getWriter().println("<a href='LogoutServlet' class='btn btn-danger mt-2'>Logout</a>");
    }
}
