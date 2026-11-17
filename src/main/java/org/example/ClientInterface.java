package org.example;

import org.example.exceptions.*;
import org.example.model.BankAccount;
import org.example.model.CheckingAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    public static void main(String[] args) throws IOException {
        ClientInterface clientInterface = new ClientInterface();
        clientInterface.start();
    }

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private final List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {
        String option;
        while (true) {
            printMainMenu();
            option = scanner.next();

            int choice;
            try {
                choice = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid choice (1 thru 6)\n");
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        addNewAccount();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 2:
                    try {
                        deleteAccount();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 3:
                    try {
                        deposit();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 4:
                    try {
                        withdraw();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 5:
                    try {
                        printInfo();
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage() + "\n");
                    }
                    break;
                case 6:
                    System.out.println("Exiting application. Goodbye!");
                    return;
                default:
                    System.err.println("Please enter a valid choice (1 thru 6)\n");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() {
        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = scanner.next();
        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Confirm password: ");
        var confirmPassword = scanner.next();

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a numeric value: " + userIdStr);
        }

        if (accounts.stream().anyMatch(a -> a.getUserId().equals(userId))) {
            throw new DuplicateAccountIdException("Account with userId " + userId + " already exists.");
        }

        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordException("Password and confirm password do not match.");
        }

        if (password.length() < 8) {
            throw new InvalidPasswordException("Password must be at least 8 characters long.");
        }

        if (!password.matches(".*\\d.*")) {
            throw new InvalidPasswordException("Password must contain at least one digit.");
        }

        CheckingAccount newAccount = new CheckingAccount(name, "ACC-" + userId, userId, password);
        accounts.add(newAccount);
        System.out.println("Account created successfully for " + name + " (User ID: " + userId + ").");
    }

    private void deleteAccount() {
        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a numeric value: " + userIdStr);
        }

        BankAccount account = findAccount(userId);
        validatePassword(account, password);

        if (account.getAccountBalance() != 0.0) {
            throw new AccountHasBalanceException(
                    "Cannot delete account with non-zero balance: " + account.getAccountBalance()
            );
        }

        accounts.remove(account);
        System.out.println("Account with User ID " + userId + " deleted successfully.");
    }

    private void deposit() {
        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a numeric value: " + userIdStr);
        }

        BankAccount account = findAccount(userId);
        validatePassword(account, password);

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a numeric value: " + amountStr);
        }

        account.deposit(amount);
        System.out.println("Successfully deposited " + amount + ". New balance: " + account.getAccountBalance());
    }

    private void withdraw() {
        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amountStr = scanner.next();

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a numeric value: " + userIdStr);
        }

        BankAccount account = findAccount(userId);
        validatePassword(account, password);

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a numeric value: " + amountStr);
        }

        account.withdraw(amount);
        System.out.println("Successfully withdrew " + amount + ". New balance: " + account.getAccountBalance());
    }

    private void printInfo() {
        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userIdStr = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new AccountException("User ID must be a numeric value: " + userIdStr);
        }

        BankAccount account = findAccount(userId);
        validatePassword(account, password);

        System.out.println("\nAccount info: ");
        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        System.out.println(" - account type: " + (account instanceof CheckingAccount ? "Checking" : "Standard"));
        if (account instanceof CheckingAccount checkingAccount) {
            System.out.println(" - account daily withdrawal limit: " + checkingAccount.getDailyWithdrawalLimit());
        }

        System.out.print("\nPress Enter to return to main menu...");
        try {
            // Consume remaining newline if any, then wait for enter
            scanner.nextLine();
            scanner.nextLine();
        } catch (Exception ignored) {
        }
    }

    private BankAccount findAccount(Long userId) {
        return accounts.stream()
                .filter(a -> a.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Account with User ID " + userId + " not found."));
    }

    private void validatePassword(BankAccount account, String password) {
        if (!account.getUserPassword().equals(password)) {
            throw new InvalidPasswordException("Incorrect password for account with User ID " + account.getUserId());
        }
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }
}
