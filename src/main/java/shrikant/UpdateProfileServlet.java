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

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // SHOW FORM
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        int studentId = (Integer) session.getAttribute("studentId");
        response.setContentType("text/html");

        try {
            Connection conn = DBConnect.getConnection();
            String sql = "SELECT * FROM students WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                response.getWriter().println("""
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>
    <style>
        body { background-color: #eef3ff; padding: 20px; }
        .edit-box {
            background: white;
            padding: 25px;
            width: 600px;
            margin: auto;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<h2 class='text-center text-primary mb-4'>Edit Profile</h2>
<div class='edit-box'>
<form method='post' action='UpdateProfileServlet'>
""");

                response.getWriter().println(
                    "<label>Name</label><input class='form-control mb-3' type='text' name='name' value='" + rs.getString("name") + "' required>" +
                    "<label>Phone</label><input class='form-control mb-3' type='text' name='phone' value='" + rs.getString("phone") + "' required>" +
                    "<label>Branch</label><input class='form-control mb-3' type='text' name='branch' value='" + rs.getString("branch") + "' required>" +
                    "<label>Year</label><input class='form-control mb-3' type='number' name='year' value='" + rs.getInt("year") + "' required>" +
                    "<label>New Password (optional)</label><input class='form-control mb-3' type='password' name='password'>"
                );

                response.getWriter().println("""
    <button type='submit' class='btn btn-primary w-100'>Save Changes</button>
</form>
</div>
</body>
</html>
""");
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // UPDATE PROFILE
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        int studentId = (Integer) session.getAttribute("studentId");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String branch = request.getParameter("branch");
        int year = Integer.parseInt(request.getParameter("year"));
        String password = request.getParameter("password");

        try {
            Connection conn = DBConnect.getConnection();

            String sql = "UPDATE students SET name=?, phone=?, branch=?, year=?"
                    + (password != null && !password.isEmpty() ? ", password=?" : "")
                    + " WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, branch);
            ps.setInt(4, year);

            int index = 5;

            if (password != null && !password.isEmpty()) {
                ps.setString(index++, password);
            }

            ps.setInt(index, studentId);

            ps.executeUpdate();
            ps.close();
            conn.close();

            response.sendRedirect("StudentViewProfileServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error updating profile: " + e.getMessage());
        }
    }
}
