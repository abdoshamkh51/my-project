package main.EWallet912.service.impl;

import main.EWallet912.enums.DepositStatus;
import main.EWallet912.model.Account;
import main.EWallet912.model.WalletSystem;
import main.EWallet912.service.AccountService;

import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private WalletSystem walletSystem = new WalletSystem();

    @Override
    public Account createAccount(Account account) {
        boolean isAccountExistWithSameUserName = walletSystem.getAccounts().stream()
                .anyMatch(acc -> acc.getUserName().equals(account.getUserName()));

        if (isAccountExistWithSameUserName) {
            return null;
        }

        walletSystem.getAccounts().add(account);
        return account;
    }

    @Override
    public Account getAccountByUsernameAndPassword(Account account) {
        Optional<Account> existedAccount = walletSystem.getAccounts().stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName()) &&
                        acc.getPassword().equals(account.getPassword())).findFirst();

        if (existedAccount.isPresent()) {
            return existedAccount.get();
        }

        return null;
    }

    @Override
    public DepositStatus deposit(Account account, double amount) {
        // 1. make sure account wallet
        Optional<Account> accountOptional = walletSystem.getAccounts().stream().filter(acc -> acc.getUserName().equals(account.getUserName())).findFirst();
        if (accountOptional.isEmpty()) {
            return DepositStatus.ACCOUNT_NOT_EXIST;
        }
        // 2. make sure amount deposit >= 100
        if (amount < 100) {
            return DepositStatus.AMOUNT_LESS_THAN_MINIMUM;
        }
        // 3. make sure amount deposit <= 12000
        if (amount > 12000) {
            return DepositStatus.AMOUNT_GREATER_THAN_MAXIMUM;
        }

        // 3. deposit feature
        double currentBalance = accountOptional.get().getBalance();
        accountOptional.get().setBalance(currentBalance + amount);
        return DepositStatus.SUCCESS;
    }

    @Override
    public Double withdraw(Account account, double amount) {
        // 1. make sure account wallet
        Optional<Account> accountOptional = walletSystem.getAccounts().stream().filter(acc -> acc.getUserName().equals(account.getUserName())).findFirst();
        if (accountOptional.isEmpty()) {
            return -1.0;
        }
        // 2. make sure amount deposit >= 100
        if (amount < 100) {
            return -2.0;
        }
        // 3. make sure amount deposit <= 12000
        if (amount > 12000) {
            return -3.0;
        }

        if (accountOptional.get().getBalance() < amount) {
            return -4.0;
        }

        double currentBalance = accountOptional.get().getBalance();
        accountOptional.get().setBalance(currentBalance - amount);
        return accountOptional.get().getBalance();
    }

    @Override
    public Double getAccountBalance(Account account) {
        Optional<Account> accountOptional = walletSystem.getAccounts().stream()
                .filter(acc -> acc.getUserName().equals(account.getUserName())).findFirst();
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("account not exist");
        }

        return accountOptional.get().getBalance();
    }

    @Override
    public Double transfer(Account account, String receiverUsername, Double amount) {
        // 1. التأكد من وجود الحساب المرسل
        if (account == null) return -1.0;

        // 2. منع التحويل لنفس الحساب
        if (account.getUserName().equals(receiverUsername)) return -2.0;

        // 3. التأكد من أن المبلغ أكبر من صفر
        if (amount <= 0) return -3.0;

        // 4. التأكد من وجود رصيد كافٍ
        if (account.getBalance() < amount) return -4.0;

        // 5. البحث عن الحساب المستلم
        Account receiver = getAccountByUsername(receiverUsername);
        if (receiver == null) return -1.0;

        // 6. خصم المبلغ من المرسل وإضافته للمستلم
        account.setBalance(account.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        return account.getBalance(); // إرجاع الرصيد الحالي بعد التحويل
    }

    private Account getAccountByUsername(String username) {
        if (username == null) {
            return null;
        }
        return walletSystem.getAccounts().stream()
                .filter(acc -> username.equalsIgnoreCase(acc.getUserName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public double getBalance(String username) {
        Account account = getAccountByUsername(username);
        if (account != null) {
            return account.getBalance();
        }
        return 0.0;
    }
}