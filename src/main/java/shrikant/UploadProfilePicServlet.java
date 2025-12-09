package shrikant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/UploadProfilePicServlet")
@MultipartConfig
public class UploadProfilePicServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect("index.html");
            return;
        }

        int studentId = (int) session.getAttribute("studentId");
        Part filePart = request.getPart("profilePic");

        if (filePart == null || filePart.getSubmittedFileName().isEmpty()) {
            response.getWriter().println("<script>alert('Please select a file to upload'); window.location.href='student_dashboard.html';</script>");
            return;
        }

        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

        // Upload folder in Tomcat
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File file = new File(uploadDir, fileName);

        // Save file and overwrite existing safely
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Save file path into DB
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "UPDATE students SET profile_pic=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            String dbFilePath = "uploads/" + fileName;

            ps.setString(1, dbFilePath);
            ps.setInt(2, studentId);
            ps.executeUpdate();

            ps.close();
            conn.close();

            response.sendRedirect("StudentViewProfileServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error uploading profile picture: " + e.getMessage());
        }
    }
}
