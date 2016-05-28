package org.bibanon.anaunet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrabSiteInitializer {

    private String igsets = "--igsets=global,";

    public Process grabSite(URL url) {
        return grabSite(url, "");
    }

    public Process grabSite(String url) {
        try {
            URL urll = new URL(url);
            return grabSite(urll, "");
        } catch (MalformedURLException ex) {
            Logger.getLogger(GrabSiteInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Process grabSite(String url, String igsets) {
        try {
            URL urll = new URL(url);
            return grabSite(urll, igsets);
        } catch (MalformedURLException ex) {
            Logger.getLogger(GrabSiteInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Process grabSite(URL url, String igsets) {
        Process process = null;
        try {
            if (igsets.isEmpty()) {
                this.igsets = "";
            } else {
                if (igsets.contains("noglobal")) {
                    this.igsets = "--igsets=";
                    igsets = igsets.replaceAll("noglobal,", "");
                }
                this.igsets += igsets.replaceAll("[&]", "\\&").replaceAll("[\"]", "\\\"").replaceAll("[;]", "\\;").replaceAll("[|]", "").replaceAll("[.][.][/]", "./").replaceAll("[$]", "\\$").replaceAll(" ", "") + " ";
            }
            process = Runtime.getRuntime().exec("grab-site " + url.toExternalForm().replaceAll("[&]", "\\&").replaceAll("[\"]", "\\\"").replaceAll("[;]", "\\;").replaceAll("[|]", "").replaceAll("[.][.][/]", "./").replaceAll("[$]", "\\$").replaceAll(" ", "") + " " + this.igsets + "&");
        } catch (IOException ex) {
            Logger.getLogger(GrabSiteInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        url = null;
        this.igsets = "--igsets=";
        return process;
    }
}
