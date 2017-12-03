
package se.kth.ict.id1212.minor.hw3.common;

import java.io.Serializable;


public interface FileDTO extends Serializable{
    public long getSize();
    public String getFileName();
}
