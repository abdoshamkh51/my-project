package main.EWallet912.service.impl;

import main.EWallet912.enums.DepositStatus;
import main.EWallet912.model.Account;
import main.EWallet912.service.AccountService;
import main.EWallet912.service.ApplicationService;

import java.util.Scanner;

public class WalletApplicationServiceImpl implements ApplicationService {

    private final AccountService accountService = new AccountServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    private Account currentUser = null;

    @Override
    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("please choose......");
            System.out.println("1.login   2.signup   3.Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    signupMenu();
                    break;
                case 3:
                    System.out.println("have a nice day :)");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void loginMenu() {
        System.out.println("Please enter your username:");
        String username = scanner.next();
        System.out.println("Please enter your password:");
        String password = scanner.next();

        Account acc = new Account(username, password);

        currentUser = accountService.getAccountByUsernameAndPassword(acc);

        if (currentUser != null) {
            System.out.println("success login :)");
            userDashboard();
        } else {
            System.out.println("login failed!");
        }
    }

    private void signupMenu() {
        System.out.println("Please enter your username:");
        String username = scanner.next();
        System.out.println("Please enter your password:");
        String password = scanner.next();
        System.out.println("Please enter your phone number:");
        String phone = scanner.next();
        System.out.println("Please enter your age:");
        float age = scanner.nextFloat();

        Account newAccount = new Account(username, password, phone, age);

        Account created = accountService.createAccount(newAccount);
        if (created != null) {
            System.out.println("account created success...");
            currentUser = created;
            userDashboard();
        } else {
            System.out.println("Failed to create account!");
        }
    }

    private void userDashboard() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("1.deposit   2.withdraw   3.Transfer   4.show balance   5.show details   6.Change Password   7.Logout");
            System.out.println("pls enter your choose");
            int option = scanner.nextInt();

            switch (option) {
                case 1: // deposit
                    System.out.println("pls enter your amount:");
                    double depositAmount = scanner.nextDouble();
                    DepositStatus status = accountService.deposit(currentUser, depositAmount);
                    if (status == DepositStatus.SUCCESS) {
                        System.out.println("Success deposit current balance: " + currentUser.getBalance());
                    } else {
                        System.out.println("Deposit failed: " + status);
                    }
                    break;

                case 2: // withdraw
                    System.out.println("pls enter amount:");
                    double withdrawAmount = scanner.nextDouble();
                    Double newBalance = accountService.withdraw(currentUser, withdrawAmount);
                    if (newBalance >= 0) {
                        System.out.println("Success withdraw current balance: " + newBalance);
                    } else {
                        System.out.println("Withdraw failed!");
                    }
                    break;

                case 3: // Transfer
                    System.out.println("pls enter receiver username:");
                    String receiverUsername = scanner.next();
                    System.out.println("pls enter amount:");
                    double transferAmount = scanner.nextDouble();

                    Double resultBalance = accountService.transfer(currentUser, receiverUsername, transferAmount);
                    if (resultBalance >= 0) {
                        System.out.println("Success transfer! Current balance: " + resultBalance);
                    } else {
                        System.out.println("Transfer failed!");
                    }
                    break;

                case 4: // show balance
                    Double balance = accountService.getAccountBalance(currentUser);
                    System.out.println("Your current balance: " + balance);
                    break;

                case 5: // show details
                    System.out.println("--- Account Details ---");
                    System.out.println("Username: " + currentUser.getUserName());
                    System.out.println("Phone: " + currentUser.getPhoneNumber());
                    System.out.println("Age: " + currentUser.getAge());
                    System.out.println("Balance: " + currentUser.getBalance());
                    break;

                case 6: // Change Password
                    System.out.println("pls enter new password:");
                    String newPassword = scanner.next();
                    currentUser.setPassword(newPassword);
                    System.out.println("Password changed successfully!");
                    break;

                case 7: // Logout
                    System.out.println("have a nice day :)");
                    currentUser = null;
                    loggedIn = false;
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}