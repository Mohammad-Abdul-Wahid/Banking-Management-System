package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    Connection connection;
    Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register() throws SQLException {
        scanner.nextLine();
        System.out.println("Full name:");
        String name = scanner.nextLine();
        System.out.println("email:");
        String email = scanner.nextLine();
        System.out.println("password:");
        String password = scanner.nextLine();
        if (user_exists(email)) {
            System.out.println("There is already an account of this email.");
            System.out.println("Please use different email");
            return;
        }
        String register_query = "insert into user(full_name,email,password) values (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(register_query);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 0) {
                System.out.println("Registered successfully");
            } else {
                System.out.println("Registration unsuccessful.");
                System.out.println("Please try again");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String login() throws SQLException {
        scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        try {
            String login_query = "select email from user where email = ? and password = ?";
            PreparedStatement ps = connection.prepareStatement(login_query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean user_exists(String email) throws SQLException {
        String query = "select 1 from user where email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
