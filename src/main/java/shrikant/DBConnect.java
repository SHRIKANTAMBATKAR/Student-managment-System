package shrikant;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                // Load Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish Connection
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/student_managmanet",
                        "root",
                        "SHRIKANT@2024"
                );

                System.out.println("Database connected successfully!");
            }

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return connection; // return the REAL connection
    }
}
