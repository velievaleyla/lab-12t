package org.example.model;

import org.example.exceptions.InsufficientFundsException;
import org.example.exceptions.InvalidAmountException;

public class BankAccount {

    private String customerName;
    private String accountNumber;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    public BankAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountBalance = 0.0;
    }

    public BankAccount(String customerName, Long userId, String userPassword) {
        this(customerName, "ACC-" + userId, userId, userPassword);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0. Provided: " + amount);
        }
        this.accountBalance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0. Provided: " + amount);
        }
        if (amount > this.accountBalance) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + this.accountBalance + ", requested: " + amount
            );
        }
        this.accountBalance -= amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance < 0) {
            throw new InvalidAmountException("Account balance cannot be negative: " + accountBalance);
        }
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "customerName='" + customerName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId=" + userId +
                ", accountBalance=" + accountBalance +
                '}';
    }
}
