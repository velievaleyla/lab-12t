package org.example;

import org.example.exceptions.*;
import org.example.model.BankAccount;
import org.example.model.CheckingAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankingTest {

    @Test
    @DisplayName("BankAccount initializes with balance 0")
    void testBankAccountInit() {
        BankAccount account = new BankAccount("John Doe", "ACC-101", 101L, "Password123");
        assertEquals("John Doe", account.getCustomerName());
        assertEquals("ACC-101", account.getAccountNumber());
        assertEquals(101L, account.getUserId());
        assertEquals("Password123", account.getUserPassword());
        assertEquals(0.0, account.getAccountBalance());
    }

    @Test
    @DisplayName("deposit adds funds to balance")
    void testDeposit() {
        BankAccount account = new BankAccount("John Doe", 101L, "Password123");
        account.deposit(150.0);
        assertEquals(150.0, account.getAccountBalance());

        assertThrows(InvalidAmountException.class, () -> account.deposit(0));
        assertThrows(InvalidAmountException.class, () -> account.deposit(-50));
    }

    @Test
    @DisplayName("withdraw deducts funds when valid")
    void testWithdraw() {
        BankAccount account = new BankAccount("John Doe", 101L, "Password123");
        account.deposit(200.0);
        account.withdraw(50.0);
        assertEquals(150.0, account.getAccountBalance());

        assertThrows(InvalidAmountException.class, () -> account.withdraw(0));
        assertThrows(InvalidAmountException.class, () -> account.withdraw(-20));
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(300.0));
    }

    @Test
    @DisplayName("CheckingAccount default daily withdrawal limit is 300")
    void testCheckingAccountDefaultLimit() {
        CheckingAccount ca = new CheckingAccount("Alice", 102L, "Pass1234");
        assertEquals(300.0, ca.getDailyWithdrawalLimit());
        assertEquals(0.0, ca.getAccountBalance());
    }

    @Test
    @DisplayName("CheckingAccount withdraw respects daily limit")
    void testCheckingAccountDailyLimit() {
        CheckingAccount ca = new CheckingAccount("Alice", 102L, "Pass1234");
        ca.deposit(1000.0);

        // Within limit
        ca.withdraw(250.0);
        assertEquals(750.0, ca.getAccountBalance());

        // Exceeds daily limit (300) even though balance is 750
        assertThrows(DailyLimitExceededException.class, () -> ca.withdraw(300.01));
        assertThrows(DailyLimitExceededException.class, () -> ca.withdraw(500.0));
    }

    @Test
    @DisplayName("CheckingAccount custom limit and setter")
    void testCheckingAccountCustomLimit() {
        CheckingAccount ca = new CheckingAccount("Alice", "ACC-102", 102L, "Pass1234", 500.0);
        assertEquals(500.0, ca.getDailyWithdrawalLimit());

        ca.setDailyWithdrawalLimit(600.0);
        assertEquals(600.0, ca.getDailyWithdrawalLimit());

        assertThrows(InvalidAmountException.class, () -> ca.setDailyWithdrawalLimit(-10.0));
    }

    @Test
    @DisplayName("CheckingAccount throws InsufficientFundsException when limit is ok but balance is low")
    void testCheckingAccountInsufficientFunds() {
        CheckingAccount ca = new CheckingAccount("Alice", 102L, "Pass1234");
        ca.deposit(50.0);

        assertThrows(InsufficientFundsException.class, () -> ca.withdraw(100.0));
    }
}
