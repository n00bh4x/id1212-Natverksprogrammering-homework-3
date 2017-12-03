/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id1212.minor.hw3.client.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author mikaelnorberg
 */
public class FileHandler {

    public static long getFileSize(String filename) throws IOException {
        Path path = Paths.get("/Users/mikaelnorberg/Documents/KTH/ID1212 Nätverksprogrammering/Homework 3/netbeans/mavenproject1/files", filename);
        long size;
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            size = attributes.size();
            return size;
        } catch (IOException e) {
            throw new IOException("The file does not exist.");
        }
    }
    
    
}
