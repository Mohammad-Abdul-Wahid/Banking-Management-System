package BankingManagementSystem;

import java.sql.Connection;
import java.util.Scanner;

public class AccountManager {
    Connection connection;
    Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number) {

    }

    public void debit_money(long account_number) {

    }

    public void transfer_money(long sender_account_number) {

    }

    public void check_balance(long account_number) {

    }
}
