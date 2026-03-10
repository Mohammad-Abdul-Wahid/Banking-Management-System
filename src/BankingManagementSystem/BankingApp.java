package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "Wahid@123";

    static void main() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            long accountNumber;
            String email;

            while (true) {
                System.out.println("Banking Management Statement");
                System.out.println("1.Register");
                System.out.println("2.Log in");
                System.out.println("3.Exit");
                System.out.println("Enter your choice: ");
                int ch = scanner.nextInt();
                switch (ch) {
                    case 1 -> {
                        user.register();
                        System.out.println("\033[H\033[2J");
                        System.out.flush();
                    }
                    case 2 -> {
                        email = user.login();
                        if (email != null) {
                            if (!accounts.account_exists(email)) {
                                System.out.println("Enter your choice");
                                System.out.println("1.Create a account");
                                System.out.println("2.Exit");
                                if (scanner.nextInt() == 1) {
                                    accountNumber = accounts.open_account(email);
                                    System.out.println("Account created successfully");
                                    System.out.println("Your account number: " + accountNumber);
                                } else {
                                    break;
                                }
                            }
                            accountNumber = accounts.get_account_number(email);
                            int ch2 = 0;
                            while (ch2 != 5) {
                                System.out.println("1.credit money");
                                System.out.println("2.debit money");
                                System.out.println("3.transfer money");
                                System.out.println("4.check money");
                                System.out.println("5.log out");
                                System.out.println("Enter your choice:");
                                ch2 = scanner.nextInt();
                                switch (ch2) {
                                    case 1 -> {
                                        accountManager.credit_money(accountNumber);
                                    }
                                    case 2 -> {
                                        accountManager.debit_money(accountNumber);
                                    }
                                    case 3 -> {
                                        accountManager.transfer_money(accountNumber);
                                    }
                                    case 4 -> {
                                        accountManager.check_balance(accountNumber);
                                    }
                                    case 5 -> {

                                    }
                                    default -> {
                                        System.out.println("Enter a valid choice");
                                    }

                                }
                            }

                        } else {
                            System.out.println("email not found");
                        }
                    }
                    case 3 -> {
                        return;
                    }
                    default -> {

                    }
                }
            }


        } catch (SQLException e) {
            e.getMessage();
        }

    }
}
