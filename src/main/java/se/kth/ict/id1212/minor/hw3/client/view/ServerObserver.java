/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.client.view;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import se.kth.ict.id1212.minor.hw3.common.ClientOutput;

/**
 *
 * @author mikaelnorberg
 */
public class ServerObserver extends UnicastRemoteObject implements ClientOutput{
    ThreadSafeStdOut out;
    public ServerObserver() throws RemoteException {
        out = new ThreadSafeStdOut();
    }
    
    public void messageToClient(String message) throws RemoteException {
        out.println(message);
    }

}
