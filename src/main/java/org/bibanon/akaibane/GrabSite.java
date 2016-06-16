package org.bibanon.akaibane;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrabSite extends Thread {

    private String igsets = "--igsets=global,";
    private Process process;
    private int pid;
    private URL url;
    private IAMetadata metadata;
    private boolean running;

    public void setGrabSite(URL url) {
        setGrabSite(url, "");
    }

    public void setGrabSite(String theurl) {
        try {
            url = new URL(theurl);
            setGrabSite(url, "");
        } catch (MalformedURLException ex) {
            Logger.getLogger(GrabSite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGrabSite(String theurl, String igsets) {
        try {
            url = new URL(theurl);
            setGrabSite(url, igsets);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GrabSite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGrabSite(URL theurl, String igsets) {
        try {
            url = new URL(theurl.toExternalForm().replaceAll("[&]", "\\&").replaceAll("[\"]", "\\\"").replaceAll("[;]", "\\;").replaceAll("[|]", "").replaceAll("[.][.][/]", "./").replaceAll("[$]", "\\$").replaceAll(" ", ""));
        } catch (MalformedURLException ex) {
            Logger.getLogger(GrabSite.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (igsets.isEmpty()) {
            this.igsets = "";
        } else {
            if (igsets.contains("noglobal")) {
                this.igsets = "--igsets=";
                igsets = igsets.replaceAll("noglobal,", "");
            }
            this.igsets += igsets.replaceAll("[&]", "\\&").replaceAll("[\"]", "\\\"").replaceAll("[;]", "\\;").replaceAll("[|]", "").replaceAll("[.][.][/]", "./").replaceAll("[$]", "\\$").replaceAll(" ", "") + " ";
        }
    }

    public void setMetadata(String data) {
        metadata = new IAMetadata(url, data);
    }
    
    public IAMetadata getMetadata() {
        return metadata;
    }

    @Override
    public void start() {
        try {
            process = Runtime.getRuntime().exec("grab-site " + url.toExternalForm() + " " + this.igsets);
            pid = getPid(process);
            running = true;
            while (process.isAlive() && running) {
                Thread.sleep(200);
            }
            if(running == false) {
                process.destroy();
            }
        } catch (IOException ex) {
            Logger.getLogger(GrabSite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GrabSite.class.getName()).log(Level.SEVERE, null, ex);
        }
        running = false;
    }

    private int getPid(Process process) {
        try {
            Class<?> cProcessImpl = process.getClass();
            Field fPid = cProcessImpl.getDeclaredField("pid");
            if (!fPid.isAccessible()) {
                fPid.setAccessible(true);
            }
            return fPid.getInt(process);
        } catch (Exception e) {
            return -1;
        }
    }
    
    public int getPid() {
        return pid;
    }
    
    public Process getProcess() {
        return process;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void stopRunning() {
        running = false;
    }
}
