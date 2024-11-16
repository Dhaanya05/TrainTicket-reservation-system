import java.sql.*;
import java.util.Scanner;

public class TrainTicketReservationSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/javadata";
    private static final String USER = "root"; // replace with your database username
    private static final String PASSWORD = "12345"; // replace with your database password
    private static Connection connection;

    public TrainTicketReservationSystem() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // User Registration
public void registerUser(String username, String password, String email, String phone) {
    String query = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, username);
        statement.setString(2, password); // In real applications, hash the password before saving
        statement.setString(3, email);
        statement.setString(4, phone);
        statement.executeUpdate();
        System.out.println("User registered successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // Train Schedule Management
    public void addTrainSchedule(int trainId, Timestamp departureTime, Timestamp arrivalTime, int availableSeats) {
        String query = "INSERT INTO schedules (train_id, departure_time, arrival_time, available_seats) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainId);
            statement.setTimestamp(2, departureTime);
            statement.setTimestamp(3, arrivalTime);
            statement.setInt(4, availableSeats);
            statement.executeUpdate();
            System.out.println("Schedule added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Online Booking Portal and Ticket Management
 
/*public void bookTicket(int userId, int scheduleId, int seatNumber) {        String query = "INSERT INTO tickets (schedule_id, user_id, seat_number, booking_status) VALUES (?, ?, ?, 'booked')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, scheduleId);
            statement.setInt(2, userId);
            statement.setInt(3, seatNumber);
            statement.executeUpdate();
            System.out.println("Ticket booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
public void bookTicket(int userId, int scheduleId, int numberOfTickets) {
    String sql = "INSERT INTO tickets (user_id, schedule_id, number_of_tickets) VALUES (?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(URL, USER,PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, userId);
        pstmt.setInt(2, scheduleId);
        pstmt.setInt(3, numberOfTickets); // This should match the column name

        pstmt.executeUpdate();
        System.out.println("Ticket booked successfully!");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}


public void updateAvailableSeats(int scheduleId, int numberOfTickets) {
    String sql = "UPDATE schedules SET available_seats = available_seats - ? WHERE train_id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, numberOfTickets);
        pstmt.setInt(2, scheduleId);
        pstmt.executeUpdate();
        System.out.println("Available seats updated successfully.");
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

    // Real-Time Availability
    public void checkAvailability(int trainId) {
        String query = "SELECT available_seats FROM schedules WHERE train_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Available Seats: " + resultSet.getInt("available_seats"));
            } else {
                System.out.println("No schedule found for this train.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Payment Processing
    public void processPayment(int reservationId, double amount) {
        String query = "INSERT INTO payments (reservation_id, amount, payment_status, payment_date) VALUES (?, ?, 'paid', NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reservationId);
            statement.setDouble(2, amount);
            statement.executeUpdate();
            System.out.println("Payment processed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Notifications and Alerts
    public void sendNotification(int userId, String message) {
        String query = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.executeUpdate();
            System.out.println("Notification sent!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// Reporting and Analytics: Display all reports

public void generateReport() {
    String query = "SELECT train_id, event_description, event_timestamp FROM analytics";
    try (Statement statement = connection.createStatement()) {
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println("Train Event Reports:");
        while (resultSet.next()) {
            int trainId = resultSet.getInt("train_id");
            String eventDescription = resultSet.getString("event_description");
            Timestamp eventTimestamp = resultSet.getTimestamp("event_timestamp");

            System.out.printf("Train ID: %d | Event: %s | Time: %s%n", 
                              trainId, eventDescription, eventTimestamp);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // Check-In and Boarding
    public void checkIn(int ticketId) {
        String query = "UPDATE tickets SET booking_status = 'checked-in' WHERE ticket_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ticketId);
            statement.executeUpdate();
            System.out.println("Check-in successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Admin Dashboard - Managing Users
    public void addAdminUser(String username, String password, String role) {
        String query = "INSERT INTO admin_users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
            System.out.println("Admin user added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ticket Management: Cancel or Update Ticket
    public void manageTicket(int ticketId, String action) {
        String query = "";
        if (action.equalsIgnoreCase("cancel")) {
            query = "UPDATE tickets SET booking_status = 'cancelled' WHERE ticket_id = ?";
        } else if (action.equalsIgnoreCase("update")) {
            query = "UPDATE tickets SET booking_status = 'updated' WHERE ticket_id = ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ticketId);
            statement.executeUpdate();
            System.out.println("Ticket " + action + " successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Reservation Tracking: Track reservation status
    public void trackReservation(int reservationId) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Process the reservation data
            } else {
                System.out.println("Reservation not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    // Train and Seat Allocation
    public void allocateSeat(int trainId, int seatNumber) {
        String query = "INSERT INTO seats (train_id, seat_number, availability) VALUES (?, ?, 'allocated')";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, trainId);
            statement.setInt(2, seatNumber);
            statement.executeUpdate();
            System.out.println("Seat allocated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Customer Support Integration: Log a support ticket
    public void logSupportTicket(int userId, String issue) {
        String query = "INSERT INTO support_tickets (user_id, issue_description, status, created_date) VALUES (?, ?, 'open', NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, issue);
            statement.executeUpdate();
            System.out.println("Support ticket logged successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Data Security and Compliance: Basic audit logging
    public void logSecurityEvent(int trainId, String eventDescription) {
        // Check if the train_id exists in schedules
        String checkQuery = "SELECT COUNT(*) FROM schedules WHERE train_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, trainId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Proceed to log the event
                String insertQuery = "INSERT INTO analytics (train_id, event) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, trainId);
                    insertStmt.setString(2, eventDescription);
                    insertStmt.executeUpdate();
                }
            } else {
                System.out.println("Invalid train_id: " + trainId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    // Display Train Schedules
public void displaySchedules() {
    String query = "SELECT * FROM schedules";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
        
        System.out.println("Train Schedules:");
        while (resultSet.next()) {
            int trainId = resultSet.getInt("train_id");
            Timestamp departureTime = resultSet.getTimestamp("departure_time");
            Timestamp arrivalTime = resultSet.getTimestamp("arrival_time");
            int availableSeats = resultSet.getInt("available_seats");
            
            System.out.printf("Train ID: %d | Departure: %s | Arrival: %s | Available Seats: %d%n", 
                              trainId, departureTime, arrivalTime, availableSeats);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // Main program to handle user input and interact with the system
    public static void main(String[] args) {
        TrainTicketReservationSystem system = new TrainTicketReservationSystem();
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("Welcome to Train Ticket Reservation System");
    
        while (true) {
            System.out.println("Choose an option: ");
            System.out.println("1. Register User");
            System.out.println("2. Add Train Schedule");
            System.out.println("3. Book Ticket");
            System.out.println("4. Check Availability");
            System.out.println("5. Process Payment");
            System.out.println("6. Send Notification");
            System.out.println("7. Generate Report");
            System.out.println("8. Check-In");
            System.out.println("9. Add Admin User");
            System.out.println("10. Manage Ticket");
            System.out.println("11. Track Reservation");
            System.out.println("12. Allocate Seat");
            System.out.println("13. Log Support Ticket");
            System.out.println("14. Log Security Event");
            System.out.println("15. Display Schedules"); // New option
            System.out.println("16. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
    
            switch (choice) {
                case 1:
                    // Register User
                    System.out.print("Enter Username: ");
                    String username = scanner.next();
                    System.out.print("Enter Password: ");
                    String password = scanner.next();
                    System.out.print("Enter Email: ");
                    String email = scanner.next();
                    System.out.print("Enter Phone Number: ");
                    String phone = scanner.next();
                    system.registerUser(username, password, email, phone);
                    break;
    
                case 2:
                    // Add Train Schedule
                    System.out.print("Enter Train ID: ");
                    int trainId = scanner.nextInt();
                    System.out.print("Enter Departure Time (yyyy-MM-dd hh:mm:ss): ");
                    scanner.nextLine(); // Clear buffer
                    String departure = scanner.nextLine();
                    System.out.print("Enter Arrival Time (yyyy-MM-dd hh:mm:ss): ");
                    String arrival = scanner.nextLine();
                    System.out.print("Enter Available Seats: ");
                    int seats = scanner.nextInt();
                    system.addTrainSchedule(trainId, Timestamp.valueOf(departure), Timestamp.valueOf(arrival), seats);
                    break;
    
                case 3:
                    // Book Ticket
                    System.out.print("Enter User ID: ");
                    int userId = scanner.nextInt();
                    System.out.print("Enter Schedule ID: ");
                    int scheduleId = scanner.nextInt();
                    System.out.print("Enter Seat Number: ");
                    int seatNumber = scanner.nextInt();
                    system.bookTicket(userId, scheduleId, seatNumber);
                    break;
    
                case 4:
                    // Check Availability
                    System.out.print("Enter Train ID: ");
                    trainId = scanner.nextInt();
                    system.checkAvailability(trainId);
                    break;
    
                case 5:
                    // Process Payment
                    System.out.print("Enter Reservation ID: ");
                    int reservationId = scanner.nextInt();
                    System.out.print("Enter Amount: ");
                    double amount = scanner.nextDouble();
                    system.processPayment(reservationId, amount);
                    break;
    
                case 6:
                    // Send Notification
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextInt();
                    System.out.print("Enter Message: ");
                    scanner.nextLine();  // Clear newline
                    String message = scanner.nextLine();
                    system.sendNotification(userId, message);
                    break;
    
                case 7:
                    // Generate and display all reports
                    system.generateReport();
                    break;
    
                case 8:
                    // Check-In
                    System.out.print("Enter Ticket ID: ");
                    int ticketId = scanner.nextInt();
                    system.checkIn(ticketId);
                    break;
    
                case 9:
                    // Add Admin User
                    System.out.print("Enter Username: ");
                    username = scanner.next();
                    System.out.print("Enter Password: ");
                    password = scanner.next();
                    System.out.print("Enter Role: ");
                    String role = scanner.next();
                    system.addAdminUser(username, password, role);
                    break;
    
                case 10:
                    // Manage Ticket
                    System.out.print("Enter Ticket ID: ");
                    ticketId = scanner.nextInt();
                    System.out.print("Enter action (cancel/update): ");
                    String action = scanner.next();
                    system.manageTicket(ticketId, action);
                    break;
    
                case 11:
                    // Track Reservation
                    System.out.print("Enter Reservation ID: ");
                    reservationId = scanner.nextInt();
                    system.trackReservation(reservationId);
                    break;
    
                case 12:
                    // Allocate Seat
                    System.out.print("Enter Train ID: ");
                    trainId = scanner.nextInt();
                    System.out.print("Enter Seat Number: ");
                    seatNumber = scanner.nextInt();
                    system.allocateSeat(trainId, seatNumber);
                    break;
    
                case 13:
                    // Log Support Ticket
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextInt();
                    System.out.print("Enter Issue Description: ");
                    scanner.nextLine();  // Clear newline
                    String issue = scanner.nextLine();
                    system.logSupportTicket(userId, issue);
                    break;
    
                case 14:
                    // Log Security Event
                    System.out.print("Enter User ID: ");
                    userId = scanner.nextInt();
                    System.out.print("Enter Event Description: ");
                    scanner.nextLine();  // Clear newline
                    String eventDescription = scanner.nextLine();
                    system.logSecurityEvent(userId, eventDescription);
                    break;
    
                    case 15:
                    // Display Schedules
                    system.displaySchedules();
                    break;
        
                case 16:
                    // Exit
                    System.out.println("Exiting system...");
                    scanner.close();
                    System.exit(0);
        
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }    
}
