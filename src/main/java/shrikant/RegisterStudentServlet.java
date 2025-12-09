package shrikant;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterStudentServlet")
public class RegisterStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String branch = request.getParameter("branch");
        int year = Integer.parseInt(request.getParameter("year"));

        try {
            Connection conn = DBConnect.getConnection();
            String sql = "INSERT INTO students (name, email, phone, password, branch, year) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, password); // Use BCrypt if required
            ps.setString(5, branch);
            ps.setInt(6, year);
            int i = ps.executeUpdate();

            if (i > 0) {
                response.sendRedirect("index.html"); // Redirect after registration
            } else {
                response.getWriter().println("Registration failed.");
            }
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
