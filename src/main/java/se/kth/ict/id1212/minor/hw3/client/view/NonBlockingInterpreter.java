/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.client.view;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import se.kth.ict.id1212.minor.hw3.client.integration.FileHandler;
import se.kth.ict.id1212.minor.hw3.common.AccountDTO;
import se.kth.ict.id1212.minor.hw3.common.FileCatalog;
import se.kth.ict.id1212.minor.hw3.client.controller.Controller;
import se.kth.ict.id1212.minor.hw3.common.FileDTO;

/**
 * Reads and interprets user commands. The command interpreter will run in a separate thread, which
 * is started by calling the <code>start</code> method. Commands are executed in a thread pool, a
 * new prompt will be displayed as soon as a command is submitted to the pool, without waiting for
 * command execution to complete.
 */
public class NonBlockingInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();
    private FileCatalog fileCatalog;
    private boolean receivingCmds = false;
    private String userId;
    private final Controller controller;

    public NonBlockingInterpreter(){
        this.controller = new Controller();
    }
    public void start(FileCatalog bank) {
        this.fileCatalog = bank;
        if (receivingCmds) {
            return;
        }
        receivingCmds = true;
        new Thread(this).start();
    }

    /**
     * Interprets and performs user commands.
     */
    @Override
    public void run() {
        AccountDTO acct = null;
        while (receivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    case QUIT:
                        receivingCmds = false;
                        break;
                    case REGISTER:
                        if(userId == null) {
                            acct = fileCatalog.createAccount(cmdLine.getParameter(0), cmdLine.getParameter(1));
                            userId = acct.getUsername();
                        } else {
                            outMgr.println("You must logout to register.");
                        }
                        break;
                    case UNREGISTER:
                        if(userId.equalsIgnoreCase(cmdLine.getParameter(0))) {
                            if(fileCatalog.deleteAccount(cmdLine.getParameter(0), cmdLine.getParameter(1))){
                                userId = null;
                            }
                        } else {
                            outMgr.println("You must be logged in to unregister.");
                        }
                        break;
                    case LOGIN:
                        if(userId == null) {
                            acct = fileCatalog.login(cmdLine.getParameter(0), cmdLine.getParameter(1));
                            userId = acct.getUsername();
                        } else {
                            outMgr.println("You must logout to login.");
                        }
                        break;    
                    case LOGOUT:
                        if(userId != null) {
                            fileCatalog.logout(userId);
                            userId = null;
                        } else {
                            outMgr.println("You must login to logout.");
                        }
                        break; 
                    case UPLOAD:
                        if(userId != null) {
                            uploadFile(userId,
                                    cmdLine.getParameter(0),
                                    cmdLine.getParameter(1),
                                    cmdLine.getParameter(2),
                                    cmdLine.getParameter(3));
                        } else {
                            outMgr.println("You must be logged in to upload a file.");
                        }
                        break;
                    case DELETE:
                        if(userId != null) {
                            fileCatalog.deleteFile(userId, cmdLine.getParameter(0));
                        } else {
                            outMgr.println("You must be logged in to delete a file.");
                        }
                        break;
                    case LIST:
                        if (userId != null) {
                            List<? extends FileDTO> files = fileCatalog.listFiles(userId);
                            for (FileDTO file : files) {
                                outMgr.println(file.toString());
                            }
                        } else {
                            outMgr.println("You must be logged in to list files.");
                        }
                        break;
                    default:
                        outMgr.println("illegal command");
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
                outMgr.println(e.getMessage());
            }
        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return console.nextLine();
    }

    
    private void uploadFile(String userId, String filename, String pAccess, String wAccess, String rAccess) throws Exception {
        boolean publicAccess;
        boolean writeAccess;
        boolean readAccess;
        try{
            publicAccess = checkAccess(pAccess);
            writeAccess = checkAccess(wAccess);
            readAccess = checkAccess(rAccess);
            long filesize = controller.getFileSize(filename);
            fileCatalog.upload(userId, filename, filesize, publicAccess, writeAccess, readAccess);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

}

    private boolean checkAccess(String pAccess) throws Exception {
        if(pAccess.equalsIgnoreCase("yes")) {
            return true;
        } else if(pAccess.equalsIgnoreCase("no")) {
            return false;
        } else {
            throw new Exception("'yes' and 'no' allowed for file access");
        }
    }
}
