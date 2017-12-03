package se.kth.ict.id1212.minor.hw3.server.controller;

import se.kth.ict.id1212.minor.hw3.server.model.AccountManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import se.kth.ict.id1212.minor.hw3.common.AccountDTO;
import se.kth.ict.id1212.minor.hw3.server.model.AccountException;
import se.kth.ict.id1212.minor.hw3.server.integration.FileCatalogDAO;
import se.kth.ict.id1212.minor.hw3.server.model.Account;
import se.kth.ict.id1212.minor.hw3.common.FileCatalog;
import se.kth.ict.id1212.minor.hw3.common.FileDTO;
import se.kth.ict.id1212.minor.hw3.server.model.File;


public class Controller extends UnicastRemoteObject implements FileCatalog {
    private final FileCatalogDAO database;
    private final AccountManager accountManager;

    public Controller() throws RemoteException {
        super();
        database = new FileCatalogDAO();
        accountManager = new AccountManager();
    }

    
    @Override
    public AccountDTO login(String username, String password) throws RemoteException, AccountException {
        Account account = null;
        try {
            if ((account = accountManager.isUserLoggedIn(username, password)) != null) {
                throw new AccountException("User already logged in.");
            } else if ((account = database.authenticateUser(username, password)) == null) {
                throw new AccountException("Wrong username or password");
            } else {
                accountManager.login(account);
            }
        } catch (Exception e) {
            throw new AccountException(e.getMessage());
        }
        return account;
    }
    
    @Override
    public void logout(String username) throws RemoteException, AccountException {
        try {
            accountManager.logout(username);
        } catch (Exception e) {
            throw new AccountException("Could not logout user");
        }
    }

    @Override
    public AccountDTO createAccount(String username, String password) throws AccountException {
        try {
            if (database.findAccountByName(username, true) != null) {
                throw new AccountException("Account already exists.");
            }
            Account account = database.createAccount(new Account(username, password));
            accountManager.login(account);
            return account;
        } catch (Exception e) {
            throw new AccountException(e.getMessage());
        }
    }



    @Override
    public boolean deleteAccount(String username, String password) throws AccountException {
        Account account = null;
        try {
            accountManager.removeLoggedInAccount(username, password);
            if ((account = database.authenticateUser(username, password)) == null) {
                throw new AccountException("Wrong username or password");
            } else {
                database.deleteAccount(username, password);
                return true;
            }
        } catch (Exception e) {
            throw new AccountException(e.getMessage());
        }
    }

    @Override
    public void upload(String userId, String filename, long filesize, boolean publicAccess, boolean writeAccess, boolean readAccess) throws RemoteException, AccountException {
        try{
            Account account = accountManager.getAccount(userId);
            database.uploadFile(new File(account, filename, filesize, publicAccess, writeAccess, readAccess));
        } catch(Exception e) {
            throw new AccountException("File already exists. file name must be unique.");
        }
    }

    @Override
    public void deleteFile(String userId, String filename) throws RemoteException, AccountException {
        try{
            database.deleteFile(userId, filename);
        } catch(Exception e) {
            throw new AccountException(e.getMessage());
        }
    }

    @Override
    public List<? extends FileDTO> listFiles(String userId) throws RemoteException, AccountException {
        return database.listFiles(userId);
    }



}