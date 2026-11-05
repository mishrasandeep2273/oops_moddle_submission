
import java.sql.*;
import java.util.Scanner;

public class HostelManagement {

    static final String URL =
            "jdbc:mysql://localhost:3306/hostel_db";
    static final String USER = "root";
    static final String PASSWORD = "College@2026";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    // Database Connection
    static void connect() throws SQLException {
        con = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Database connected successfully!");
    }

    // (a) Insert at least three records
    static void insertStudents() throws SQLException {

        String sql = "INSERT INTO HostelStudent VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            Object[][] students = {
                    {1, "Rahul Sharma", "2401", "Boys Hostel A", "101", 60000.0},
                    {2, "Aman Verma", "2402", "Boys Hostel B", "202", 55000.0},
                    {3, "Priya Singh", "2403", "Girls Hostel A", "303", 65000.0}
            };

            for (Object[] student : students) {

                ps.setInt(1, (Integer) student[0]);
                ps.setString(2, (String) student[1]);
                ps.setString(3, (String) student[2]);
                ps.setString(4, (String) student[3]);
                ps.setString(5, (String) student[4]);
                ps.setDouble(6, (Double) student[5]);

                try {
                    ps.executeUpdate();
                    System.out.println("Student inserted successfully!");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate student ID: " + student[0]);
                }
            }
        }
    }

    // (b) Conditional Retrieval and Sorting
    static void retrieveStudents() throws SQLException {

        System.out.print("Enter hostel name: ");
        String hostel = sc.nextLine();

        String sql = "SELECT * FROM HostelStudent " +
                "WHERE hostel_name = ? AND fee > 50000 " +
                "ORDER BY fee DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hostel);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("student_id") + " | " +
                                rs.getString("student_name") + " | " +
                                rs.getString("roll_number") + " | " +
                                rs.getString("hostel_name") + " | " +
                                rs.getString("room_number") + " | " +
                                rs.getDouble("fee")
                );
            }
        }
    }

    // (c) Update Hostel Fee
    static void updateFee() throws SQLException {

        System.out.print("Enter student ID: ");
        int id = sc.nextInt();

        System.out.print("Enter new hostel fee: ");
        double fee = sc.nextDouble();
        sc.nextLine();

        String sql = "UPDATE HostelStudent SET fee = ? WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, fee);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Hostel fee updated successfully!");
            } else {
                System.out.println("Student record does not exist.");
            }
        }
    }

    // (d) Search and Modify Hostel Details
    static void updateRoom() throws SQLException {

        System.out.print("Enter student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql = "SELECT * FROM HostelStudent WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.print("Enter new room number: ");
                String room = sc.nextLine();

                String updateSql =
                        "UPDATE HostelStudent SET room_number = ? " +
                                "WHERE student_id = ?";

                try (PreparedStatement updatePs =
                             con.prepareStatement(updateSql)) {

                    updatePs.setString(1, room);
                    updatePs.setInt(2, id);

                    updatePs.executeUpdate();
                }

                System.out.println("Updated Student Details:");

                System.out.println(
                        "ID: " + rs.getInt("student_id") +
                                "\nName: " + rs.getString("student_name") +
                                "\nRoll Number: " + rs.getString("roll_number") +
                                "\nHostel: " + rs.getString("hostel_name") +
                                "\nRoom Number: " + room +
                                "\nFee: " + rs.getDouble("fee")
                );

            } else {
                System.out.println("Student does not exist.");
            }
        }
    }

    // (e) Aggregate Query and Conditional Deletion
    static void deleteStudent() throws SQLException {

        String sumSql = "SELECT SUM(fee) FROM HostelStudent";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sumSql)) {

            if (rs.next()) {
                System.out.println(
                        "Total Hostel Fee Collected: " + rs.getDouble(1)
                );
            }
        }

        System.out.print("Enter student ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql =
                "DELETE FROM HostelStudent WHERE student_id = ? AND fee > 0";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println(
                        "Student not found or hostel fee is not fully paid."
                );
            }
        }
    }

    // Main Menu
    public static void main(String[] args) {

        try {
            connect();

            int choice;

            do {
                System.out.println("\n===== HOSTEL MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Hostel Student Records");
                System.out.println("2. Retrieve Students by Hostel");
                System.out.println("3. Update Hostel Fee");
                System.out.println("4. Search and Update Room Number");
                System.out.println("5. Total Fee and Delete Student");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        insertStudents();
                        break;

                    case 2:
                        retrieveStudents();
                        break;

                    case 3:
                        updateFee();
                        break;

                    case 4:
                        updateRoom();
                        break;

                    case 5:
                        deleteStudent();
                        break;

                    case 6:
                        System.out.println("Exiting application...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } while (choice != 6);

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
