/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mikaelnorberg
 */
public interface ClientOutput extends Remote {
    public void messageToClient(String message) throws RemoteException;
}
