/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bibanon.akaibane;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class IAGrabSiteProcessManager extends Thread {

    private static HashMap<Integer, GrabSite> grabsites = new HashMap<Integer, GrabSite>();
    private static HashMap<Integer, IAUpload> internetarchives = new HashMap<Integer, IAUpload>();
    private static ArrayList<Integer> grabsiteskeys = new ArrayList<Integer>();
    private static ArrayList<GrabSite> unusedgs = new ArrayList<GrabSite>();
    private static ArrayList<IAUpload> unusedia = new ArrayList<IAUpload>();
    private static boolean running = true;
    //...
    private static int gspid, iapid = 0;
    private static IAUpload ia = null;
    private static GrabSite gs = null;
    private static IAMetadata metadata = null;
    private static Process process = null;

    public IAGrabSiteProcessManager() {
    }

    public int addGrab(String url, String igsets, String meta) {
        if (unusedgs.isEmpty()) {
            gs = new GrabSite();
        } else {
            synchronized (unusedgs) {
                gs = unusedgs.remove(0);
            }
        }
        gs.setGrabSite(url, igsets);
        gs.setMetadata(meta);
        gs.start();
        gspid = gs.getPid();

        addGrabProcess(gs);

        gs = null;
        return gspid;
    }

    public void addGrabProcess(GrabSite grab) {
        synchronized (grabsites) {
            for (int i = grabsites.size(); i < grabsites.size(); i++) {
                if (grabsites.containsKey(gspid) || grabsites.containsValue(grab)) {
                    return;
                }
            }
            grabsites.put(gspid, grab);
            grabsiteskeys.add(gspid);
        }
    }

    public void toIAProcess(Integer pid) {

        synchronized (internetarchives) {
            for (int i = internetarchives.size(); i < internetarchives.size(); i++) {
                if (internetarchives.containsKey(pid) || internetarchives.containsValue(ia)) {
                    return;
                }
            }

            synchronized (grabsites) {
                metadata = grabsites.get(pid).getMetadata();
            }
            if (unusedia.isEmpty()) {
                ia = new IAUpload();
            } else {
                synchronized (unusedia) {
                    ia = unusedia.remove(0);
                }
            }
            // TODO
            internetarchives.put(pid, ia);
        }
        ia = null;
    }

    @Override
    public void run() {
        running = true;
        
        while (running) {
            try {
                Thread.sleep(50);
                for (int i = 0; i < grabsiteskeys.size(); i++) {

                    if (!grabsites.get(grabsiteskeys.get(i)).getProcess().isAlive()) {
                        grabsites.get(grabsiteskeys.get(i)).stopRunning();
                        unusedgs.add(grabsites.remove(grabsiteskeys.get(i)));
                    }
                }
                int j = grabsiteskeys.size();
                for(int i = 0; i < j; i++) {
                    if(!grabsites.containsKey(grabsiteskeys.get(i))) {
                        grabsiteskeys.remove(i);
                        j--;
                        i--;
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(IAGrabSiteProcessManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void die() {
        running = false;
    }

}
