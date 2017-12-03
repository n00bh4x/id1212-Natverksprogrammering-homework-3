/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.server.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author mikaelnorberg
 */
public class AccountManager {
    private final Map<String, Account> loggedInAccounts;
    
    public AccountManager() {
        loggedInAccounts = new ConcurrentHashMap<>();
    }
    
    public void login(Account account) {
        String id = account.getUsername();
        loggedInAccounts.put(id, account);
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
        if(loggedInAccounts.containsKey(username)) {
            loggedInAccounts.remove(username);
        }
    }

    public void removeLoggedInAccount(String username, String password) {
        Account account;
        if(loggedInAccounts.containsKey(username)) {
            account = loggedInAccounts.get(username);
            if(password.matches(account.getPassword())) {
                loggedInAccounts.remove(username);
            }
        }
    }
    
    public Account getAccount(String username) throws AccountException {
        Account account = null;
        if ((account = loggedInAccounts.get(username)) == null) {
            throw new AccountException("Something whent wrong. User does not exist.");
        }
        return account;
    }
}
