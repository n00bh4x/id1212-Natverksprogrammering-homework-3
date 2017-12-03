/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.client.controller;

import java.io.IOException;
import se.kth.ict.id1212.minor.hw3.client.integration.FileHandler;

/**
 *
 * @author mikaelnorberg
 */
public class Controller {
    
    public Controller() {
    }
    
    public long getFileSize(String filename) throws IOException{
        return FileHandler.getFileSize(filename);
    }
}
