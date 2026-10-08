package main.EWallet912.service;

import main.EWallet912.enums.DepositStatus;
import main.EWallet912.enums.TransferStatus;
import main.EWallet912.model.Account;

public interface AccountService {
    Account createAccount(Account account);

    Account getAccountByUsernameAndPassword(Account account);

    DepositStatus deposit(Account account, double amount);
    Double withdraw(Account account, double amount);

    Double getAccountBalance(Account account);
    Double transfer(Account account, String receiverUsername, Double amount);
    double getBalance(String username);

}