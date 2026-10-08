package main.EWallet912;

import main.EWallet912.model.Account;
import main.EWallet912.service.AccountService;
import main.EWallet912.service.ApplicationService;
import main.EWallet912.service.impl.AccountServiceImpl;
import main.EWallet912.service.impl.WalletApplicationServiceImpl;



public class main {
    public static void main(String[] args)  {
        new WalletApplicationServiceImpl().start();

    }
    }

