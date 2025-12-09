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

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);


        response.setContentType("text/html");

        try {
            Connection conn = DBConnect.getConnection();

            String sql = """
                SELECT s.id, s.name, s.branch, s.year,
                       a.status, a.date
                FROM students s
                LEFT JOIN attendance a ON s.id = a.student_id
                AND a.date = (SELECT MAX(date) FROM attendance WHERE student_id = s.id)
                ORDER BY s.id ASC;
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            response.getWriter().println("""
<!DOCTYPE html>
<html>
<head>
    <title>Attendance Records</title>

    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css' rel='stylesheet'>

    <style>
        body {
            background-color: #eef3ff;
            padding: 20px;
        }
        .container-box {
            background: white;
            padding: 25px;
            border-radius: 10px;
            width: 90%;
            margin: auto;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        h3 {
            color: #0d6efd;
            font-weight: 600;
            margin-bottom: 20px;
            text-align: center;
        }
        table {
            border-radius: 8px;
            overflow: hidden;
        }
        thead th {
            background-color: #0d6efd !important;
            color: white;
            text-align: center;
        }
        tbody tr:hover {
            background-color: #e7f0ff;
        }
        td, th {
            vertical-align: middle !important;
            text-align: center;
        }
    </style>
</head>

<body>

<div class='container-box'>
    <h3>Attendance Records</h3>

    <table class='table table-bordered table-striped'>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Branch</th>
                <th>Year</th>
                <th>Last Marked Date</th>
                <th>Status</th>
            </tr>
        </thead>

        <tbody>
""");

            while (rs.next()) {
                String date = rs.getString("date");
                String status = rs.getString("status");

                response.getWriter().println("<tr>");
                response.getWriter().println("<td>" + rs.getInt("id") + "</td>");
                response.getWriter().println("<td>" + rs.getString("name") + "</td>");
                response.getWriter().println("<td>" + rs.getString("branch") + "</td>");
                response.getWriter().println("<td>" + rs.getInt("year") + "</td>");
                response.getWriter().println("<td>" + (date != null ? date : "No record") + "</td>");

                // Color badge for status
                if (status == null) {
                    response.getWriter().println("<td><span class='badge bg-secondary'>Not Marked</span></td>");
                } else if (status.equals("Present")) {
                    response.getWriter().println("<td><span class='badge bg-success'>Present</span></td>");
                } else {
                    response.getWriter().println("<td><span class='badge bg-danger'>Absent</span></td>");
                }

                response.getWriter().println("</tr>");
            }

            response.getWriter().println("""
        </tbody>
    </table>
</div>

<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'></script>

</body>
</html>
""");

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
