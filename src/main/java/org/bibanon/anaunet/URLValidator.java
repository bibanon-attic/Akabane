/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bibanon.anaunet;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author root
 */
public class URLValidator {

    private static final Pattern regex = Pattern.compile("^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}\\.([a-z]+)?[a-zA-Z0-9/_.~+&=-]*$‌​");

    public boolean isValid(URL url) {
        return isValid(url.toExternalForm());
    }

    public boolean isValid(String url) {
        Matcher m;
        m = regex.matcher(url);
        return m.matches();
    }
}
