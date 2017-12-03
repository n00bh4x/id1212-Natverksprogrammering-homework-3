package se.kth.ict.id1212.minor.hw3.server.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import se.kth.ict.id1212.minor.hw3.server.controller.Controller;
import se.kth.ict.id1212.minor.hw3.common.FileCatalog;


public class Server {
    private static final String USAGE = "java bankjpa.Server [bank name in rmi registry]";
    private String catalogName = FileCatalog.CATALOG_NAME_IN_REGISTRY;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.parseCommandLineArgs(args);
            server.startRMIServant();
            System.out.println("Server started.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Failed to start bank server.");
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller contr = new Controller();
        Naming.rebind(catalogName, contr);
    }

    private void parseCommandLineArgs(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.length > 0) {
            catalogName = args[0];
        }
    }
}