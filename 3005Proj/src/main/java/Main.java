import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:postgresql://localhost:5432/projectFitness" +
            "";
    private static final String user = "postgres";
    private static final String pass = "postgres";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        register(scanner);
                        break;
                    case 2:
                        login(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3: ");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void register(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.println("Register as: 1. Member, 2. Trainer, 3. Admin");
        System.out.print("Enter your choice: ");
        int userType = scanner.nextInt();
        scanner.nextLine();

        switch (userType) {
            case 1:
                registerMember(scanner);
                break;
            case 2:
                registerTrainer(scanner);
                break;
            case 3:
                registerAdmin(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3: ");
        }
    }

    private static void login(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.println("Login as: 1. Member, 2. Trainer, 3. Admin");
        System.out.print("Enter your choice: ");
        int userType = scanner.nextInt();
        scanner.nextLine();
        int userId = 0;
        switch (userType) {
            case 1:
                System.out.print("Enter your ID: ");
                userId = scanner.nextInt();
                scanner.nextLine();
                if(verifyMember(userId)){
                    memberMenu(scanner, userId);
                } else {
                    System.out.println("Invalid id entered, please try again");
                    break;
                }
                break;
            case 2:
                System.out.print("Enter your ID: ");
                userId = scanner.nextInt();
                scanner.nextLine();
                if(verifyTrainer(userId)){
                    trainerMenu(scanner, userId);
                } else {
                    System.out.println("Invalid id entered, please try again");
                    break;
                }
                break;
            case 3:
                System.out.print("Enter your ID: ");
                userId = scanner.nextInt();
                scanner.nextLine();
                if(verifyAdmin(userId)){
                    adminMenu(scanner, userId);
                } else {
                    System.out.println("Invalid id entered, please try again");
                    break;
                }
                break;
            default:
                System.out.println("Invalid choice; Please enter a number between 1 and 3");
        }

    }

    private static void registerMember(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter goal weight (kg): ");
        float goalWeight = scanner.nextFloat();
        System.out.print("Enter current weight (kg): ");
        float weight = scanner.nextFloat();
        System.out.print("Enter height (cm): ");
        float height = scanner.nextFloat();
        scanner.nextLine();

        float bmi = (float) (weight / Math.pow((height/100), 2));

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Members (balance, mem_name, email_address, goal_weight, bmi, weight_kg, height_cm) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setFloat(1, 20.99F);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setFloat(4, goalWeight);
            pstmt.setFloat(5, bmi);
            pstmt.setFloat(6, weight);
            pstmt.setFloat(7, height);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Member registered successfully starting at $20.99/per month");
            } else {
                System.out.println("Failed to register member");
            }
        }
    }

    private static void registerTrainer(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.print("Enter trainer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter availability (1 for weekdays, 2 for weekends, 3 for both): ");
        int availability = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Trainer (train_name, availability) VALUES (?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setInt(2, availability);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Trainer registered successfully");
            } else {
                System.out.println("Failed to register trainer");
            }
        }
    }

    private static void registerAdmin(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO Administrator (full_name) VALUES (?)")) {
            pstmt.setString(1, fullName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Administrator registered successfully");
            } else {
                System.out.println("Failed to register administrator");
            }
        }
    }

    private static boolean verifyMember(int userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT exists (SELECT 1 FROM members WHERE member_id = "+userId+" LIMIT 1);")) {
            resultSet.next();
            return resultSet.getBoolean("exists");
        }
    }

    private static boolean verifyTrainer(int userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT exists (SELECT 1 FROM trainer WHERE trainer_id = "+userId+" LIMIT 1);")) {
            resultSet.next();
            return resultSet.getBoolean("exists");
        }
    }

    private static boolean verifyAdmin(int userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT exists (SELECT 1 FROM administrator WHERE admin_id = "+userId+" LIMIT 1);")) {
            resultSet.next();
            return resultSet.getBoolean("exists");
        }
    }
    private static void memberMenu(Scanner scanner, int memberId) throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM members WHERE member_id = "+memberId+" LIMIT 1;")) {
            resultSet.next();
            System.out.println("Welcome "+resultSet.getString("mem_name"));
        }
        while (true) {
            System.out.println();
            System.out.println("Member Menu:");
            System.out.println("1. Display Information");
            System.out.println("2. Update Information");
            System.out.println("3. Display Exercise Routines");
            System.out.println("4. Add Exercise Routine");
            System.out.println("5. Display Achievements");
            System.out.println("6. Add Achievement");
            System.out.println("7. Schedule Training / Classes");
            System.out.println("8. Display Existing Training / Classes");
            System.out.println("9. Cancel Existing Training / Classes");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayMemberInformation(memberId);
                    break;
                case 2:
                    updateMemberInformation(scanner, memberId);
                    break;
                case 3:
                    displayExerciseRoutines(memberId);
                    break;
                case 4:
                    addExerciseRoutine(scanner, memberId);
                    break;
                case 5:
                    displayAchievements(memberId);
                    break;
                case 6:
                    addAchievement(scanner, memberId);
                    break;
                case 7:
                    scheduleTraining(scanner, memberId);
                    break;
                case 8:
                    displayTrainingSessions(memberId);
                    break;
                case 9:
                    deleteTrainingSession(scanner, memberId);
                    break;
                case 10:
                    return;
                default:
                    System.out.println("Invalid choice; Please enter a number between 1 and 10: ");
            }
        }
    }

    private static void displayMemberInformation(int memberId) throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM members WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            System.out.println("Balance: " + rs.getFloat("balance"));
            System.out.println("Name: " + rs.getString("mem_name"));
            System.out.println("Email Address: " + rs.getString("email_address"));
            System.out.println("Goal Weight: " + rs.getFloat("goal_weight"));
            System.out.println("BMI: " + rs.getFloat("BMI"));
            System.out.println("Current Weight: " + rs.getFloat("weight_kg"));
            System.out.println("Height: " + rs.getFloat("height_cm"));
        }
    }
    private static void updateMemberInformation(Scanner scanner, int memberId) throws SQLException {
        System.out.println();
        System.out.println("Select the information you would like to update:");
        System.out.println("1. Name");
        System.out.println("2. Email Address");
        System.out.println("3. Goal Weight");
        System.out.println("4. Current Weight");
        System.out.println("5. Height");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "";
            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    sql = "UPDATE Members SET mem_name = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setInt(2, memberId);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Member information updated successfully");
                        } else {
                            System.out.println("Failed to update member information");
                        }
                    }
                    break;
                case 2:
                    System.out.print("Enter new email address: ");
                    String email = scanner.nextLine();
                    sql = "UPDATE Members SET email_address = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, email);
                        pstmt.setInt(2, memberId);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Member information updated successfully");
                        } else {
                            System.out.println("Failed to update member information");
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter new goal weight (kg): ");
                    float goalWeight = scanner.nextFloat();
                    scanner.nextLine();
                    sql = "UPDATE Members SET goal_weight = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setFloat(1, goalWeight);
                        pstmt.setInt(2, memberId);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Member information updated successfully");
                        } else {
                            System.out.println("Failed to update member information");
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter new current weight (kg): ");
                    float weight = scanner.nextFloat();
                    scanner.nextLine();
                    sql = "UPDATE Members SET weight_kg = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setFloat(1, weight);
                        pstmt.setInt(2, memberId);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Member information updated successfully");
                        } else {
                            System.out.println("Failed to update member information");
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter new height (cm): ");
                    float height = scanner.nextFloat();
                    scanner.nextLine();
                    sql = "UPDATE Members SET height_cm = ? WHERE member_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setFloat(1, height);
                        pstmt.setInt(2, memberId);
                        int rowsAffected = pstmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Member information updated successfully");
                        } else {
                            System.out.println("Failed to update member information");
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice; Please enter a number between 1 and 5");
                    break;
            }
        }
    }

    private static void displayExerciseRoutines(int memberId) throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("SELECT routine_description FROM Ex_Routine WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("routine_description"));
                }
            }
        }
    }


    private static void addExerciseRoutine(Scanner scanner, int memberId) throws SQLException {
        System.out.println();
        System.out.print("Enter routine description: ");
        String routineDescription = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Ex_Routine (member_id, routine_description) VALUES (?, ?)")) {
            pstmt.setInt(1, memberId);
            pstmt.setString(2, routineDescription);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Exercise routine added successfully");
            } else {
                System.out.println("Failed to add exercise routine");
            }
        }
    }

    private static void displayAchievements(int memberId) throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("SELECT achievement_description FROM achievements WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("achievement_description"));
                }
            }
        }
    }


    private static void addAchievement(Scanner scanner, int memberId) throws SQLException {
        System.out.println();
        System.out.print("Enter achievement description: ");
        String achievementDescription = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO achievements (member_id, achievement_description) VALUES (?, ?)")) {
            pstmt.setInt(1, memberId);
            pstmt.setString(2, achievementDescription);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Achievement added successfully");
            } else {
                System.out.println("Failed to add achievement");
            }
        }
    }
    private static void scheduleTraining(Scanner scanner, int memberId) throws SQLException {
        System.out.println("Choose the type of activity:");
        System.out.println("1. Personal Training Session");
        System.out.println("2. Join a Class");
        System.out.print("Enter your choice: ");
        int activityChoice = scanner.nextInt();
        scanner.nextLine();

        if (activityChoice == 1) {
            bookPersonalTraining(scanner, memberId);
        } else if (activityChoice == 2) {
            joinClass(scanner, memberId);
        } else {
            System.out.println("Invalid choice; Please enter 1 or 2: ");
        }
    }

    private static void bookPersonalTraining(Scanner scanner, int memberId) throws SQLException {
        displayAvailableTrainers();
        System.out.print("Enter the trainer ID you wish to schedule with: ");
        int trainerId = scanner.nextInt();
        System.out.println("Choose a day (1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat, 7-Sun): ");
        int day = scanner.nextInt();
        scanner.nextLine();

        if (checkTrainerAvailability(trainerId, day)) {
                try (Connection connection = DriverManager.getConnection(url, user, pass);
                     PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Trains (member_id, trainer_id, timeslot, price) VALUES (?, ?, ?, ?)")) {
                    pstmt.setInt(1, memberId);
                    pstmt.setInt(2, trainerId);
                    pstmt.setInt(3, day);
                    pstmt.setFloat(4, 49.99F);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        PreparedStatement pstmt2 = connection.prepareStatement("UPDATE members SET balance = balance + 49.99 WHERE member_id ="+memberId);{
                            pstmt2.executeUpdate();
                        }
                        System.out.println("Training session scheduled successfully");
                    } else {
                        System.out.println("Failed to schedule training session");
                    }
                }
            } else {
                System.out.println("Trainer is not available on the selected day or is already booked");
            }
        }

    private static void joinClass(Scanner scanner, int memberId) throws SQLException {
        viewClassSchedule();
        System.out.print("Enter the class ID you wish to join: ");
        int classId = scanner.nextInt();
        scanner.nextLine();

        if (registerMemberToClass(memberId, classId)) {
            System.out.println("Successfully joined the class");
        } else {
            System.out.println("Failed to join the class. Please try again");
        }
    }
    private static boolean registerMemberToClass(int memberId, int classId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO Class_Members (class_id, member_id) VALUES (?, ?)")) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    private static void displayAvailableTrainers() throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT trainer_id, train_name, availability FROM Trainer")) {
            while (rs.next()) {
                System.out.println("Trainer ID: " + rs.getInt("trainer_id") + ", Name: " + rs.getString("train_name") +
                        ", Availability: " + rs.getInt("availability"));
            }
        }
    }

    private static boolean checkTrainerAvailability(int trainerId, int day) throws SQLException {
        System.out.println();
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("SELECT availability FROM Trainer WHERE trainer_id = ?")) {
            pstmt.setInt(1, trainerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int availability = rs.getInt("availability");
                    if (availability == 3) {
                        return checkTrainerSchedule(trainerId, day);
                    } else if(availability == 2 && day != 6 && day != 7){
                        System.out.println("Trainer only works on weekends");
                        return false;
                    } else if(availability == 1 && day != 1 && day != 2 && day != 3 && day != 4 && day != 5){
                        System.out.println("Trainer only works on weekdays");
                        return false;
                    } else if(availability == 2 && day == 6 || day == 7){
                        return checkTrainerSchedule(trainerId, day);
                    } else if(availability == 1 && day == 1 || day == 2 || day == 3 || day == 4 || day == 5){
                        return checkTrainerSchedule(trainerId, day);
                    }
                }
            }
        }
        return false;
    }

    private static boolean checkTrainerSchedule(int trainerId, int day) throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {

            try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM Trains WHERE trainer_id = ? AND timeslot = ?")) {
                pstmt.setInt(1, trainerId);
                pstmt.setInt(2, day);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            try (PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) FROM Teaches WHERE trainer_id = ? AND timeslot = ?")) {
                pstmt.setInt(1, trainerId);
                pstmt.setInt(2, day);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void displayTrainingSessions(int memberId) throws SQLException {
        System.out.println();
        System.out.println("Scheduled Training Sessions:");
        boolean hasTrainingSessions = displayPersonalTrainingSessions(memberId);
        boolean hasClasses = displayMemberClasses(memberId);

        if (!hasTrainingSessions && !hasClasses) {
            System.out.println("No training sessions or classes scheduled.");
        }
    }

    private static boolean displayPersonalTrainingSessions(int memberId) throws SQLException {
        boolean hasSessions = false;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT Trains.timeslot, Trainer.train_name, Trainer.trainer_id FROM Trains " +
                             "JOIN Trainer ON Trains.trainer_id = Trainer.trainer_id " +
                             "WHERE Trains.member_id = ? ORDER BY Trains.timeslot")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    hasSessions = true;
                    String trainerName = rs.getString("train_name");
                    int trainerId = rs.getInt("trainer_id");
                    int timeslot = rs.getInt("timeslot");
                    System.out.println("Personal Training on " + getDayName(timeslot) + " with " + trainerName + " (Trainer ID: " + trainerId + ")");
                }
            }
        }
        return hasSessions;
    }

    private static boolean displayMemberClasses(int memberId) throws SQLException {
        boolean hasClasses = false;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT FitClass.class_id, Trainer.train_name, Room.room_id, Teaches.timeslot FROM Class_Members " +
                             "JOIN FitClass ON Class_Members.class_id = FitClass.class_id " +
                             "JOIN Trainer ON FitClass.trainer_id = Trainer.trainer_id " +
                             "JOIN Teaches ON FitClass.class_id = Teaches.class_id " +
                             "JOIN Room ON FitClass.room_id = Room.room_id " +
                             "WHERE Class_Members.member_id = ?")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    hasClasses = true;
                    int classId = rs.getInt("class_id");
                    String trainerName = rs.getString("train_name");
                    int roomId = rs.getInt("room_id");
                    int timeslot = rs.getInt("timeslot");
                    System.out.println("Class (ID: " + classId + ") in Room " + roomId + " on " + getDayName(timeslot) + " with Trainer " + trainerName);
                }
            }
        }
        return hasClasses;
    }

    private static String getDayName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
        }
        return null;
    }

    private static void deleteTrainingSession(Scanner scanner, int memberId) throws SQLException {
        System.out.println("Select the type of session to cancel: ");
        System.out.println("1. Personal Training Session");
        System.out.println("2. Class Registration");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            cancelPersonalTraining(scanner, memberId);
        } else if (choice == 2) {
            cancelClassRegistration(scanner, memberId);
        } else {
            System.out.println("Invalid choice; Please enter 1 or 2: ");
        }
    }

    private static void cancelPersonalTraining(Scanner scanner, int memberId) throws SQLException {
        displayTrainingSessions(memberId);
        System.out.print("Enter the session ID of the training you wish to cancel: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();

        if (deletePersonalTrainingSession(sessionId, memberId)) {
            System.out.println("Personal training session cancelled successfully");
        } else {
            System.out.println("Failed to cancel training session");
        }
    }

    private static boolean deletePersonalTrainingSession(int sessionId, int memberId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Trains WHERE train_id = ? AND member_id = ?")) {
            pstmt.setInt(1, sessionId);
            pstmt.setInt(2, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private static void cancelClassRegistration(Scanner scanner, int memberId) throws SQLException {
        displayMemberClasses(memberId);
        System.out.print("Enter the class ID you wish to withdraw from: ");
        int classId = scanner.nextInt();
        scanner.nextLine();

        if (withdrawFromClass(classId, memberId)) {
            System.out.println("Successfully withdrawn from the class.");
        } else {
            System.out.println("Failed to withdraw from the class. Please try again");
        }
    }

    private static boolean withdrawFromClass(int classId, int memberId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Class_Members WHERE class_id = ? AND member_id = ?")) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }



    private static void trainerMenu(Scanner scanner, int trainerId) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("Trainer Menu:");
            System.out.println("1. Update Availability");
            System.out.println("2. View Member by ID");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    updateTrainerAvailability(scanner, trainerId);
                    break;
                case 2:
                    viewMemberById(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    private static void updateTrainerAvailability(Scanner scanner, int trainerId) throws SQLException {
        System.out.println();
        System.out.println("Select new availability:");
        System.out.println("1. Weekdays Only");
        System.out.println("2. Weekends Only");
        System.out.println("3. Both Weekdays and Weekends");
        System.out.print("Enter your choice (1, 2, or 3): ");
        int newAvailability = scanner.nextInt();
        scanner.nextLine();

        if (newAvailability < 1 || newAvailability > 3) {
            System.out.println("Invalid choice. Please enter a valid number (1, 2, or 3).");
            return;
        }

        if (setNewAvailability(trainerId, newAvailability)) {
            System.out.println("Trainer availability updated successfully.");
        } else {
            System.out.println("Failed to update trainer availability.");
        }
    }

    private static boolean setNewAvailability(int trainerId, int newAvailability) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("UPDATE Trainer SET availability = ? WHERE trainer_id = ?")) {
            pstmt.setInt(1, newAvailability);
            pstmt.setInt(2, trainerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private static void viewMemberById(Scanner scanner) throws SQLException {
        System.out.println();
        System.out.print("Enter the member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement("SELECT mem_name, email_address, goal_weight, BMI, weight_kg, height_cm FROM Members WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.isBeforeFirst()) {
                    System.out.println("No member found with ID: " + memberId);
                    return;
                }
                while (rs.next()) {
                    System.out.println("Name: " + rs.getString("mem_name"));
                    System.out.println("Email Address: " + rs.getString("email_address"));
                    System.out.println("Goal Weight: " + rs.getFloat("goal_weight"));
                    System.out.println("BMI: " + rs.getFloat("BMI"));
                    System.out.println("Current Weight: " + rs.getFloat("weight_kg"));
                    System.out.println("Height: " + rs.getFloat("height_cm"));
                }
            }
        }

    private static void adminMenu(Scanner scanner, int adminId) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("Admin Menu:");
            System.out.println("1. View Class Schedule");
            System.out.println("2. Book Class");
            System.out.println("3. Cancel Class");
            System.out.println("4. View All Equipment");
            System.out.println("5. View All Rooms");
            System.out.println("6. Process Payment");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewClassSchedule();
                    break;
                case 2:
                    bookClass(scanner, adminId);
                    break;
                case 3:
                    cancelClass(scanner);
                    break;
                case 4:
                    viewAllEquipment();
                    break;
                case 5:
                    viewAllRooms();
                    break;
                case 6:
                    processPayment(scanner, adminId);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
    private static void viewClassSchedule() throws SQLException {
        System.out.println("Scheduled Classes:");
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT FitClass.class_id, Trainer.train_name, Room.room_id, Teaches.timeslot " +
                             "FROM FitClass " +
                             "JOIN Trainer ON FitClass.trainer_id = Trainer.trainer_id " +
                             "JOIN Teaches ON FitClass.class_id = Teaches.class_id " +
                             "JOIN Room ON FitClass.room_id = Room.room_id")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No classes scheduled.");
                    return;
                }
                while (rs.next()) {
                    int classId = rs.getInt("class_id");
                    String trainerName = rs.getString("train_name");
                    int roomId = rs.getInt("room_id");
                    int timeslot = rs.getInt("timeslot");
                    System.out.println("Class ID: " + classId + ", Trainer: " + trainerName +
                            ", Room ID: " + roomId + ", Day: " + getDayName(timeslot));
                }
            }
        }
    }


private static void bookClass(Scanner scanner, int adminId) throws SQLException {

    System.out.print("Enter the trainer ID for the class: ");
    int trainerId = scanner.nextInt();
    scanner.nextLine();

    System.out.print("Enter the room ID for the class: ");
    int roomId = scanner.nextInt();
    System.out.println("Choose a day for the class (1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat, 7-Sun): ");
    int day = scanner.nextInt();
    scanner.nextLine();

    if (checkTrainerAvailability(trainerId, day) && checkRoomAvailability(roomId, day)) {
        if (scheduleClass(trainerId, roomId, day)) {
            System.out.println("Class booked successfully");
        } else {
            System.out.println("Failed to book class");
        }
    } else {
        System.out.println("Trainer or room is not available on the selected day");
    }
}
    private static boolean checkRoomAvailability(int roomId, int day) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT COUNT(*) FROM FitClass " +
                             "JOIN Teaches ON FitClass.class_id = Teaches.class_id " +
                             "WHERE FitClass.room_id = ? AND Teaches.timeslot = ?")) {
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, day);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return false;
    }


    private static boolean scheduleClass(int trainerId, int roomId, int day) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO FitClass (trainer_id, room_id) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, roomId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long classId = rs.getLong(1);
                        return linkClassToTimeslot(classId, trainerId, day);
                    }
                }
            }
        }
        return false;
    }
    private static boolean linkClassToTimeslot(long classId, int trainerId, int day) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO Teaches (class_id, trainer_id, timeslot) VALUES (?, ?, ?)")) {
            pstmt.setLong(1, classId);
            pstmt.setInt(2, trainerId);
            pstmt.setInt(3, day);
            return pstmt.executeUpdate() > 0;
        }
    }


    private static void cancelClass(Scanner scanner) throws SQLException {

        System.out.print("Enter the ID of the class you wish to cancel: ");
        int classId = scanner.nextInt();
        scanner.nextLine();

        if (deleteScheduledClass(classId)) {
            System.out.println("Class cancelled successfully.");
        } else {
            System.out.println("Failed to cancel class.");
        }
    }
    private static boolean deleteScheduledClass(int classId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            try (PreparedStatement pstmtClassMembers = connection.prepareStatement("DELETE FROM Class_Members WHERE class_id = ?")) {
                pstmtClassMembers.setInt(1, classId);
                pstmtClassMembers.executeUpdate();
            }

            try (PreparedStatement pstmtTeaches = connection.prepareStatement("DELETE FROM Teaches WHERE class_id = ?")) {
                pstmtTeaches.setInt(1, classId);
                pstmtTeaches.executeUpdate();
            }

            try (PreparedStatement pstmtFitClass = connection.prepareStatement("DELETE FROM FitClass WHERE class_id = ?")) {
                pstmtFitClass.setInt(1, classId);
                int rowsAffected = pstmtFitClass.executeUpdate();
                return rowsAffected > 0;
            }
        }
    }




    private static void viewAllEquipment() throws SQLException {
        System.out.println("List of All Gym Equipment: ");
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT equipment_id, description, maintenance_date FROM Equipment")) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No equipment found in the database");
                return;
            }
            while (rs.next()) {
                int equipmentId = rs.getInt("equipment_id");
                String description = rs.getString("description");
                Date maintenanceDate = rs.getDate("maintenance_date");
                System.out.println("Equipment ID: " + equipmentId + ", Description: " + description +
                        ", Last Maintenance Date: " + maintenanceDate);
            }
        }
    }

    private static void viewAllRooms() throws SQLException {
        System.out.println("List of All Rooms: ");
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT room_id FROM Room")) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No rooms found in the database");
                return;
            }
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                System.out.println("Room ID: " + roomId + ", Max Capacity: ");
            }
        }
    }
    private static void processPayment(Scanner scanner, int adminId) throws SQLException {
        System.out.print("Enter the member ID to process payment: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();

        float totalAmount = displaySessionsAndTotal(memberId);
        if (totalAmount == 0) {
            System.out.println("No booked sessions found for this member or member does not exist");
            return;
        }
        if (updateMemberBalance(memberId) && recordPayment(memberId, adminId, totalAmount)) {
            System.out.println("Payment processed successfully. Balance reset to monthly membership fee of $20.99");
            System.out.println("Payment of $" + totalAmount + " has been recorded");
        } else {
            System.out.println("Failed to process payment");
        }
    }

    private static float displaySessionsAndTotal(int memberId) throws SQLException {
        float totalAmount = 20.99F;
        boolean hasSessions = false;
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT Trains.timeslot, Trainer.train_name, Trains.price FROM Trains " +
                             "JOIN Trainer ON Trains.trainer_id = Trainer.trainer_id " +
                             "WHERE Trains.member_id = ?")) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    return 0;
                }
                System.out.println("Booked Sessions:");
                while (rs.next()) {
                    int timeslot = rs.getInt("timeslot");
                    String trainerName = rs.getString("train_name");
                    float price = rs.getFloat("price");
                    System.out.println("Day: " + getDayName(timeslot) + ", Trainer: " + trainerName + ", Price: $" + price);
                    totalAmount += price;
                    hasSessions = true;
                }
            }
        }
        return hasSessions ? totalAmount : 0;
    }

    private static boolean recordPayment(int memberId, int adminId, float amount) throws SQLException {
        java.sql.Date paymentDate = new java.sql.Date(System.currentTimeMillis());
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "INSERT INTO Payment (member_id, admin_id, amount, payment_date) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, adminId);
            pstmt.setFloat(3, amount);
            pstmt.setDate(4, paymentDate);
            return pstmt.executeUpdate() > 0;
        }
    }
    private static boolean updateMemberBalance(int memberId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = connection.prepareStatement(
                     "UPDATE Members SET balance = 20.99 WHERE member_id = ?")) {
            pstmt.setInt(1, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}

