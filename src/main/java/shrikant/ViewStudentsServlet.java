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

@WebServlet("/ViewStudentsServlet")
public class ViewStudentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Only admin can access this page
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        response.setContentType("text/html");

        // Start of HTML
        response.getWriter().println("""
<!DOCTYPE html>
<html>
<head>
    <title>All Students</title>
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>
    <style>
        body {
            background-color: #f4f7fc;
            padding: 20px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #0d6efd;
        }
        .container-box {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        table {
            margin-top: 20px;
        }
        thead th {
            background-color: #0d6efd !important;
            color: white;
            text-align: center;
        }
        tbody tr:hover {
            background-color: #e8f0ff;
        }
        td, th {
            vertical-align: middle !important;
        }
    </style>
</head>
<body>
    <h2>All Students</h2>
    <div class='container-box'>
""");

        try {
            Connection conn = DBConnect.getConnection();
            String sql = "SELECT * FROM students";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Table start
            StringBuilder table = new StringBuilder();
            table.append("<table class='table table-bordered table-striped'>");
            table.append("<thead><tr>");
            table.append("<th>ID</th><th>Name</th><th>Email</th><th>Phone</th><th>Branch</th><th>Year</th><th>Actions</th>");
            table.append("</tr></thead><tbody>");

            // Loop through students
            while (rs.next()) {
                table.append("<tr>");
                table.append("<td>").append(rs.getInt("id")).append("</td>");
                table.append("<td>").append(rs.getString("name")).append("</td>");
                table.append("<td>").append(rs.getString("email")).append("</td>");
                table.append("<td>").append(rs.getString("phone")).append("</td>");
                table.append("<td>").append(rs.getString("branch")).append("</td>");
                table.append("<td>").append(rs.getInt("year")).append("</td>");
                table.append("<td>")
                        .append("<a href='UpdateProfileServlet?id=").append(rs.getInt("id"))
                        .append("' class='btn btn-sm btn-warning me-2'>Edit</a>")
                        .append("<a href='DeleteStudentServlet?id=").append(rs.getInt("id"))
                        .append("' class='btn btn-sm btn-danger'>Delete</a>")
                        .append("</td>");
                table.append("</tr>");
            }

            table.append("</tbody></table>");
            response.getWriter().println(table.toString());

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }

        // End of HTML
        response.getWriter().println("""
    </div>
</body>
</html>
""");
    }
}
