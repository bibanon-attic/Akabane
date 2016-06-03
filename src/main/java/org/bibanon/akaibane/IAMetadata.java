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
public class IAMetadata {
    private URL url;
    private String data;
    private String iametadatas = "--metadata=\"title:WARC: ";
    public IAMetadata(URL theurl, String thedata) {
        url = theurl;
        data = thedata.replaceAll("[&]", "\\&").replaceAll("[\"]", "").replaceAll("[$]", "\\$").replaceAll(" ", "");
        buildString();
    }
    
    private void buildString() {
        String[] sitename = url.toExternalForm().split("//?");
        iametadatas += sitename[1].toUpperCase() + " \" --metadata=\"subject:warcarchives";
        if(data.length() > 0) {
            iametadatas += ";" + data;
        }
        iametadatas += "\"";
    }
    
    public String iaMetadata() {
        return iametadatas;
    }
}
