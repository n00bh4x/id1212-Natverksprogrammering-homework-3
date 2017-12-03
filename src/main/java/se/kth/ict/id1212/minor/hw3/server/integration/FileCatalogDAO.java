package se.kth.ict.id1212.minor.hw3.server.integration;



import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import se.kth.ict.id1212.minor.hw3.server.model.File;
import se.kth.ict.id1212.minor.hw3.server.model.Account;

public class FileCatalogDAO {
    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    /**
     * Constructs a new DAO object connected to the specified database.
     */
    public FileCatalogDAO() {
        emFactory = Persistence.createEntityManagerFactory("percistanceUnit");
    }


    public Account findAccountByName(String username, boolean endTransactionAfterSearching) {
        if (username == null) {
            return null;
        }

        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findAccountByName", Account.class).
                        setParameter("username", username).getSingleResult();
            } catch (NoResultException noSuchAccount) {
                return null;
            }
        } finally {
            if (endTransactionAfterSearching) {
                commitTransaction();
            }
        }
    }
    
    
    public Account authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("authenticateUser", Account.class).
                        setParameter("username", username).
                        setParameter("password", password).
                        getSingleResult();
                
            } catch (NoResultException noSuchAccount) {
                return null;
            }
        } finally {
            commitTransaction();
        }
    }    

    /**
     * Retrieves all existing accounts.
     *
     * @return A list with all existing accounts. The list is empty if there are no accounts.
     */
    public List<File> findAllAccounts() {
        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findAllAccounts", File.class).getResultList();
            } catch (NoResultException noSuchAccount) {
                return new ArrayList<>();
            }
        } finally {
            commitTransaction();
        }
    }
    
    
    public List<File> listFiles(String userId) {
        try {
            EntityManager em = beginTransaction();
            try {
                return em.createNamedQuery("findAllFilesForUser", File.class).
                        setParameter("username", userId).
                        setParameter("access", true).
                        getResultList();
            } catch (NoResultException noFiles) {
                return new ArrayList<>();
            }
        } finally {
            commitTransaction();
        }
    }

    
    
    public void uploadFile(File file) {
        try {
            EntityManager em = beginTransaction();
            em.persist(file);
            System.out.println();
        } finally {
            commitTransaction();
        }
    }

    /**
     * Creates a new account.
     *
     * @param account The account to create.
     * @return 
     */
    public Account createAccount(Account account) {
        try {
            EntityManager em = beginTransaction();
            em.persist(account);
        } finally {
            commitTransaction();
        }
        return account;
    }
    
    
    public void deleteAccount(String username, String password) {
        deleteAllFiles(username);
        try {
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteAccount", Account.class).
                    setParameter("username", username).
                    setParameter("password", password).
                    executeUpdate();
        } finally {
            commitTransaction();
        }
    }
    
    private void deleteAllFiles(String username) {
        try {
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteAllFiles", File.class).
                    setParameter("username", username).
                    executeUpdate();
        } finally {
            commitTransaction();
        }
    }
    
    public void deleteFile(String userId, String filename) {
        try {
            EntityManager em = beginTransaction();
            em.createNamedQuery("deleteFile", File.class).
                    setParameter("username", userId).
                    setParameter("filename", filename).
                    executeUpdate();
        } finally {
            commitTransaction();
        }
    }


    public void updateAccount() {
        commitTransaction();
    }




    private EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        threadLocalEntityManager.set(em);
        EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return em;
    }

    private void commitTransaction() {
        threadLocalEntityManager.get().getTransaction().commit();
    }


}