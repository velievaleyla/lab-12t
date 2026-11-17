package org.example.model;

import org.example.exceptions.DailyLimitExceededException;
import org.example.exceptions.InvalidAmountException;

public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalLimit;

    public CheckingAccount(String customerName, String accountNumber, Long userId, String userPassword) {
        super(customerName, accountNumber, userId, userPassword);
        this.dailyWithdrawalLimit = 300.0;
    }

    public CheckingAccount(String customerName, String accountNumber, Long userId, String userPassword, double dailyWithdrawalLimit) {
        super(customerName, accountNumber, userId, userPassword);
        if (dailyWithdrawalLimit < 0) {
            throw new InvalidAmountException("Daily withdrawal limit cannot be negative: " + dailyWithdrawalLimit);
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public CheckingAccount(String customerName, Long userId, String userPassword) {
        super(customerName, userId, userPassword);
        this.dailyWithdrawalLimit = 300.0;
    }

    @Override
    public void deposit(double amount) {
        super.deposit(amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0. Provided: " + amount);
        }
        if (amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                    "Withdrawal amount " + amount + " exceeds daily withdrawal limit of " + dailyWithdrawalLimit
            );
        }
        super.withdraw(amount);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        if (dailyWithdrawalLimit < 0) {
            throw new InvalidAmountException("Daily withdrawal limit cannot be negative: " + dailyWithdrawalLimit);
        }
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "dailyWithdrawalLimit=" + dailyWithdrawalLimit +
                ", " + super.toString() +
                '}';
    }
}
