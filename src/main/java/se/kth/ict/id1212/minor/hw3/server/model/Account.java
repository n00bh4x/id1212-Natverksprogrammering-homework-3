package se.kth.ict.id1212.minor.hw3.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import se.kth.ict.id1212.minor.hw3.common.AccountDTO;

@NamedQueries({

        @NamedQuery(
                name = "findAccountByName",
                query = "SELECT acct FROM Account acct WHERE acct.name LIKE :username",
                lockMode = LockModeType.OPTIMISTIC
        ),
        @NamedQuery(
                name = "authenticateUser",
                query = "SELECT acct FROM Account acct WHERE acct.name LIKE :username AND acct.password LIKE :password",
                lockMode = LockModeType.OPTIMISTIC
        ),
        @NamedQuery(
                name = "deleteAccount",
                query = "DELETE FROM Account acct WHERE acct.name LIKE :username AND acct.password LIKE :password"
        )        
})


@Entity(name = "Account")
public class Account implements AccountDTO {

    @Id
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<File> files;
    

    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;

    public Account() {
        this(null,null);
    }

    public Account(String name, String password) {
        this.name = name;
        this.password = password;
        files = new ArrayList<>();
    }


    @Override
    public String getUsername() {
        return name;
    }
    
    public void addFile(File file) {
        this.files.add(file);
    }

    String getPassword() {
        return password;
    }
}