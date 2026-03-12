package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection connection;
    Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) {

        System.out.println("Enter amount you want to credit:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin");
        String security_pin = scanner.nextLine();

        try {
            String query = "select 1 from accounts where account_number = ? and security_pin = ? ";
            PreparedStatement ps1 = connection.prepareStatement(query);
            ps1.setLong(1, account_number);
            ps1.setString(2, security_pin);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                connection.setAutoCommit(false);
                String credit_query = "update accounts set balance = balance + ? where account_number = ? ";
                PreparedStatement ps2 = connection.prepareStatement(credit_query);
                ps2.setDouble(1, amount);
                ps2.setLong(2, account_number);
                ps2.setString(3, security_pin);
                int rowsAffected = ps2.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Credited successfully");
                    connection.commit();
                    connection.setAutoCommit(true);
                } else {
                    System.out.println("Failed to credit");
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void debit_money(long account_number) {

        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter security pin:");
        String security_pin = scanner.nextLine();
        try {

            String query = "select 1 from accounts where account_number = ? and security_pin = ? ";
            PreparedStatement ps1 = connection.prepareStatement(query);
            ps1.setLong(1, account_number);
            ps1.setString(2, security_pin);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                connection.setAutoCommit(false);
                String check_balance_query = "select balance from accounts where account_number = ? ";
                PreparedStatement ps2 = connection.prepareStatement(check_balance_query);
                ps2.setLong(1, account_number);
                ps2.setString(2, security_pin);
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    double balance = rs2.getDouble("balance");

                    if (amount <= balance) {
                        String debit_query = "update accounts set balance = balance - ? where account_number = ? ";
                        PreparedStatement ps3 = connection.prepareStatement(debit_query);
                        ps3.setDouble(1, amount);
                        ps3.setLong(2, account_number);
                        ps3.setString(3, security_pin);
                        int rowsAffected = ps3.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Debited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Debit unsuccessful");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient Balance");
                    }

                }
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void transfer_money(long sender_account_number) {
        scanner.nextLine();
        System.out.println("Enter receiver account number:");
        long receiver_account_number = scanner.nextLong();
        System.out.println("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter your security pin:");
        String security_pin = scanner.nextLine();
        try {
            String query = "select * from accounts where account_number = ? and security_pin = ? ";
            PreparedStatement ps1 = connection.prepareStatement(query);
            ps1.setLong(1, sender_account_number);
            ps1.setString(2, security_pin);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                connection.setAutoCommit(false);
                String query2 = "select 1 from accounts where account_number = ?";
                PreparedStatement ps2 = connection.prepareStatement(query2);
                ps2.setLong(1, receiver_account_number);
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    double balance = rs1.getDouble("balance");

                    if (amount <= balance) {
                        String debit_query = "update accounts set balance = balance - ? where account_number = ?";
                        String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                        PreparedStatement debit_ps = connection.prepareStatement(debit_query);
                        PreparedStatement credit_ps = connection.prepareStatement(credit_query);
                        debit_ps.setDouble(1, amount);
                        debit_ps.setLong(2, sender_account_number);
                        credit_ps.setDouble(1, amount);
                        credit_ps.setLong(2, receiver_account_number);
                        int rowsAffected1 = debit_ps.executeUpdate();
                        int rowsAffected2 = credit_ps.executeUpdate();

                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transfer successful");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else {
                            System.out.println("Transfer unsuccessful");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient balance");
                    }

                } else {
                    System.out.println("Invalid receiver account number");
                }
                connection.setAutoCommit(true);
            } else {
                System.out.println("Invalid pin");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void check_balance(long account_number) {
        try {
            scanner.nextLine();
            System.out.println("Enter security pin:");
            String security_pin = scanner.nextLine();
            String check_balance_query = "select balance from accounts where account_number = ? and security_pin = ?";
            PreparedStatement ps = connection.prepareStatement(check_balance_query);
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Total Balance: " + balance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
