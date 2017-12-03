/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.common;

import java.io.Serializable;

/**
 *
 * @author mikaelnorberg
 */
public interface FileDTO extends Serializable{
    public long getSize();
    public String getFileName();
}
