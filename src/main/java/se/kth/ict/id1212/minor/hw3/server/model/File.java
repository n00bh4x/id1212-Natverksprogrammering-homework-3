package se.kth.ict.id1212.minor.hw3.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.LockModeType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import se.kth.ict.id1212.minor.hw3.common.FileDTO;


@NamedQueries({
        @NamedQuery(
                name = "deleteFile",
                query = "DELETE FROM File file WHERE file.filename LIKE :filename AND file.owner.name LIKE :username"
        ),     
        @NamedQuery(
                name = "deleteAllFiles",
                query = "DELETE FROM File file WHERE file.owner.name LIKE :username"
        ),
        @NamedQuery(
                name = "findAllFilesForUser",
                query = "SELECT file FROM File file WHERE file.owner.name LIKE :username OR file.publicAccess = :access",
                lockMode = LockModeType.OPTIMISTIC
        )
})

/**
 * An account in the bank.
 */
@Entity(name = "File")
public class File implements FileDTO {
    @Id
    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "publicAccess", nullable = false)
    private boolean publicAccess;

    @Column(name = "writeAccess", nullable = false)
    private boolean writeAccess;
    
    @Column(name = "readAccess", nullable = false)
    private boolean readAccess;
    
    @Column(name = "size", nullable = false)
    private long size;
    
    @ManyToOne()
    @JoinColumn(name = "owner")
    private Account owner;

    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;


    public File() {
    }


    public File(Account owner, String filename, long size, boolean publicAccess, boolean writeAccess, boolean readAccess) {
        this.owner = owner;
        this.filename = filename;
        this.size = size;
        this.publicAccess = publicAccess;
        this.writeAccess = writeAccess;
        this.readAccess = readAccess;
    }
    
    @Override
    public long getSize() {
        return size;
    }




    private String accountInfo() {
        return " " + this;
    }


    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append("File: [");
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("filename: ");
        stringRepresentation.append(filename);
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("owner: ");
        stringRepresentation.append(owner.getUsername());
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("size: ");
        stringRepresentation.append(size);
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("public access : ");
        stringRepresentation.append(publicAccess);
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("write access: ");
        stringRepresentation.append(writeAccess);
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("read access: ");
        stringRepresentation.append(readAccess);        
        stringRepresentation.append(System.getProperty("line.separator"));
        stringRepresentation.append("]");
        return stringRepresentation.toString();
    }

    @Override
    public String getFileName() {
        return filename;
    }
    

}