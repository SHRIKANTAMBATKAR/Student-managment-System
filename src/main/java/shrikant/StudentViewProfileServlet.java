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

@WebServlet("/StudentViewProfileServlet")
public class StudentViewProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        int studentId = (Integer) session.getAttribute("studentId");
        response.setContentType("text/html");

        response.getWriter().println("""
<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>
    <style>
        body { background-color: #f4f7fc; padding: 20px; }
        .profile-box {
            background: white;
            padding: 25px;
            border-radius: 10px;
            width: 600px;
            margin: auto;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        h2 { text-align: center; color: #0d6efd; margin-bottom: 20px; }
        .profile-img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            object-fit: cover;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<h2>My Profile</h2>
<div class='profile-box'>
""");

        try {
            Connection conn = DBConnect.getConnection();
            String sql = "SELECT * FROM students WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String pic = rs.getString("profile_pic");
                if (pic == null || pic.isEmpty()) {
                    pic = "https://via.placeholder.com/120";
                }

                response.getWriter().println(
                    "<center><img src='" + pic + "' class='profile-img'></center>"
                );

                response.getWriter().println(
                    "<table class='table table-bordered'>" +
                    "<tr><th>Name</th><td>" + rs.getString("name") + "</td></tr>" +
                    "<tr><th>Email</th><td>" + rs.getString("email") + "</td></tr>" +
                    "<tr><th>Phone</th><td>" + rs.getString("phone") + "</td></tr>" +
                    "<tr><th>Branch</th><td>" + rs.getString("branch") + "</td></tr>" +
                    "<tr><th>Year</th><td>" + rs.getInt("year") + "</td></tr>" +
                    "</table>"
                );

                response.getWriter().println(
                    "<a href='UpdateProfileServlet' class='btn btn-primary w-100 mt-3'>Edit Profile</a>"
                );
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        response.getWriter().println("""
</div>
</body>
</html>
""");
    }
}
