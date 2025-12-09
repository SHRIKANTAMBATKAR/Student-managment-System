package shrikant;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String roleCheck = request.getParameter("role");

        HttpSession session = request.getSession();

        try (Connection conn = DBConnect.getConnection()) {

            if (conn == null) {
                response.getWriter().println("Database not connected!");
                return;
            }

            PreparedStatement ps;
            ResultSet rs;

            if ("on".equals(roleCheck)) {

                // Admin login
                ps = conn.prepareStatement("SELECT * FROM admin WHERE email=? AND password=?");
                ps.setString(1, email);
                ps.setString(2, password);

                rs = ps.executeQuery();

                if (rs.next()) {
                    session.setAttribute("user", rs.getString("name"));
                    session.setAttribute("role", "admin");
                    response.sendRedirect("admin_dashboard.html");

                } else {
                    
                    response.getWriter().println("""
                        <script>
                            alert('Invalid admin credentials!');
                            window.location.href = 'index.html';
                        </script>
                    """);
                }

            } else {

                // Student login
                ps = conn.prepareStatement("SELECT * FROM students WHERE email=? AND password=?");
                ps.setString(1, email);
                ps.setString(2, password);

                rs = ps.executeQuery();

                if (rs.next()) {
                    session.setAttribute("user", rs.getString("name"));
                    session.setAttribute("role", "student");
                    session.setAttribute("studentId", rs.getInt("id"));
                    response.sendRedirect("student_dashboard.html");

                } else {
                    
                    // Show alert and return to login page
                response.getWriter().println("""
                        <script>
                            alert('Invalid student credentials!');
                            window.location.href = 'index.html';
                        </script>
                    """);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Login failed: " + e.getMessage());
        }
    }
}
