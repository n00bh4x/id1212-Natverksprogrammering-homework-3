/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.client.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import se.kth.ict.id1212.minor.hw3.client.integration.FileHandler;
import se.kth.ict.id1212.minor.hw3.client.view.NonBlockingInterpreter;
import se.kth.ict.id1212.minor.hw3.common.FileCatalog;

/**
 * Starts the chat client.
 */
public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
            FileCatalog bank = (FileCatalog) Naming.lookup(FileCatalog.CATALOG_NAME_IN_REGISTRY);
            FileHandler fileHandler = new FileHandler();
            new NonBlockingInterpreter().start(bank);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.out.println("Could not start bank client.");
        }
    }
}