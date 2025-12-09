package shrikant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/Gradeservlet")
public class Gradeservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // A simple model class to store grade details
    public static class GradeRecord {
        public int studentId;
        public String name;
        public String branch;
        public int year;
        public String subject;
        public int marks;

        public String getGrade() {
            if (marks >= 90) return "A+";
            if (marks >= 80) return "A";
            if (marks >= 70) return "B";
            if (marks >= 60) return "C";
            if (marks >= 50) return "D";
            return "F";
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Only admin can access
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        List<GradeRecord> gradeList = new ArrayList<>();

        try {
            Connection conn = DBConnect.getConnection();

            String sql = """
                SELECT g.student_id, s.name, s.branch, s.year,
                       g.subject, g.marks
                FROM grades g
                INNER JOIN students s ON g.student_id = s.id
                ORDER BY g.student_id ASC;
                """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                GradeRecord record = new GradeRecord();
                record.studentId = rs.getInt("student_id");
                record.name = rs.getString("name");
                record.branch = rs.getString("branch");
                record.year = rs.getInt("year");
                record.subject = rs.getString("subject");
                record.marks = rs.getInt("marks");

                gradeList.add(record);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Database Error: " + e.getMessage());
            return;
        }

        // Send data to JSP
        request.setAttribute("grades", gradeList);
        request.getRequestDispatcher("grades.jsp").forward(request, response);
    }
}
