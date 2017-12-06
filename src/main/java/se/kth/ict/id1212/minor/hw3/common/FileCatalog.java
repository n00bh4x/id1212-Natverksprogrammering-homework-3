package se.kth.ict.id1212.minor.hw3.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import se.kth.ict.id1212.minor.hw3.server.model.AccountException;


public interface FileCatalog extends Remote {

    public static final String CATALOG_NAME_IN_REGISTRY = "catalog";
    
    public AccountDTO createAccount(ClientOutput toClient, String username, String password) throws RemoteException, AccountException;
    
    public AccountDTO login(ClientOutput toClient, String username, String password) throws RemoteException, AccountException;

    public void logout(String username) throws RemoteException, AccountException;
    
    public boolean deleteAccount(String username, String password) throws RemoteException, AccountException;

    public void upload(boolean alertOwner, String userId, String filename, long filesize, boolean publicAccess, boolean writeAccess, boolean readAccess) throws RemoteException, AccountException;

    public void deleteFile(String userId, String filename) throws RemoteException, AccountException;

    public List<? extends FileDTO> listFiles(String userId) throws RemoteException, AccountException;
}