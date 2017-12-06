/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.server.model;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import se.kth.ict.id1212.minor.hw3.common.ClientOutput;

/**
 *
 * @author mikaelnorberg
 */
public class AccountManager {
    private final Map<String, Account> loggedInAccounts;
    private final Map<String, ClientOutput> outputToClients;
    private final Map<String, File> notifyOnAccessFiles;
    
    public AccountManager() {
        loggedInAccounts = new ConcurrentHashMap<>();
        outputToClients = new ConcurrentHashMap<>();
        notifyOnAccessFiles = new ConcurrentHashMap<>();
    }
    

    
    
    public void deleteFromNotifyOnAction(String filename) {
        notifyOnAccessFiles.remove(filename);
    }
    
    
    public void notifyOwner(String userId, String ACTION, String filename) throws RemoteException {
        File file;
        ClientOutput out;        
        file = notifyOnAccessFiles.get(filename);
        if(file != null) {
            if(!file.getOwnerName().equalsIgnoreCase(userId)) {
                out = outputToClients.get(file.getOwnerName());
                if(out != null){
                    out.messageToClient("user: " + userId + " performed action: " + ACTION  + " on file: " + filename);
                }
            }
        }
    }
    
    public void notifyOwners(String userId, String ACTION, List<File> files) throws RemoteException {
        for (File file : files) {
            notifyOwner(userId, ACTION, file.getFileName());
        }
    }
    
    
    public void addNotifyOnAccess(String filename, File file) {
        notifyOnAccessFiles.put(filename, file);
    }
    
    public void login(Account account, ClientOutput toClient) {
        String id = account.getUsername();
        loggedInAccounts.put(id, account);
        outputToClients.put(id, toClient);
    }

    public Account isUserLoggedIn(String username, String password) {
        Account account = null;
        if(loggedInAccounts.containsKey(username)){
            account = loggedInAccounts.get(username);
            if (!password.matches(account.getPassword())) {
                account = null;
            }
        }
        return account;
    }
    
    public void logout(String username) {
        loggedInAccounts.remove(username);
        outputToClients.remove(username);
    }

    public void removeLoggedInAccount(String username, String password) {
        Account account;
        if(loggedInAccounts.containsKey(username)) {
            account = loggedInAccounts.get(username);
            if(password.matches(account.getPassword())) {
                logout(username);
                for (String filename : notifyOnAccessFiles.keySet()) {
                    if(notifyOnAccessFiles.get(filename).getOwnerName().equalsIgnoreCase(username)) {
                        notifyOnAccessFiles.remove(filename);
                    }
                }
            }
        }
    }

    
    public Account getAccount(String username) throws AccountException {
        Account account;
        if ((account = loggedInAccounts.get(username)) == null) {
            throw new AccountException("Something whent wrong. User does not exist.");
        }
        return account;
    }






}
