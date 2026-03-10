package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    Connection connection;
    Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) {
        scanner.nextLine();
        System.out.println("Enter your name:");
        String full_name = scanner.nextLine();
        System.out.println("Enter your initial balance:");
        double balance = scanner.nextDouble();
        System.out.println("Enter your 4 digit pin:");
        String security_pin = scanner.nextLine();
        long accountNumber = generate_account_number();
        try {
            String open_account_query = "insert into accounts(account_number,full_name,email,balance,security_pin) values (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(open_account_query);
            ps.setLong(1, accountNumber);
            ps.setString(2, full_name);
            ps.setString(3, email);
            ps.setDouble(4, balance);
            ps.setString(5, security_pin);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return accountNumber;
            } else {
                throw new RuntimeException("Account creation failed!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long get_account_number(String email) {
        try {
            String get_account_number_query = "select account_number from accounts where email=?";
            PreparedStatement ps = connection.prepareStatement(get_account_number_query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("account_number");
            } else {
                throw new RuntimeException("Invalid email id, no accounts exists");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long generate_account_number() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number from accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exists(String email) {
        String query = "select 1 from accounts where email = ?";
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
