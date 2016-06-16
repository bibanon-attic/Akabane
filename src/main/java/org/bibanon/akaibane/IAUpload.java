/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bibanon.akaibane;

import java.net.URL;

/**
 *
 * @author root
 */
public class IAUpload extends Thread {

    private Process process;
    private int pid;
    private URL url;
    private IAMetadata metadata;
    private boolean running;
    
    public IAUpload() {
        ;
    }
    
    
}
