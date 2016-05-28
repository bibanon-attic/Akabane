package org.bibanon.anaunet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ArchiveIsHtmlParser {

    static String mainURL = "https://archive.is";
    static String postURL = "https://archive.is/submit/";
    private CloseableHttpClient httpclient;
    private CloseableHttpResponse mainResponse;
    private HttpEntity mainPage;
    private Document doc;
    private String submitid;
    private String responseStatus;
    private Header[] postRespHeaders;

    public void init() {
        httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(mainURL);
        try {
            mainResponse = httpclient.execute(httpGet);
            System.out.println(mainResponse.getStatusLine());
            mainPage = mainResponse.getEntity();
            doc = Jsoup.parse(mainPage.getContent(), "UTF-8", mainURL);
            EntityUtils.consume(mainPage);
        } catch (IOException ex) {
            Logger.getLogger(ArchiveIsHtmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                mainResponse.close();
            } catch (IOException ex) {
                Logger.getLogger(ArchiveIsHtmlParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Element content = doc.getElementById("submiturl");
        submitid = content.getElementsByAttribute("submitid").text();
    }

    public String submitURL(String Url) {
        try {
            HttpPost httpPost = new HttpPost(postURL);
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("submitid", submitid));
            nvps.add(new BasicNameValuePair("url", Url));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            try (CloseableHttpResponse postresponse = httpclient.execute(httpPost)) {
                responseStatus = postresponse.getStatusLine().getReasonPhrase();
                HttpEntity entity2 = postresponse.getEntity();
                postRespHeaders = postresponse.getHeaders("Location");
                
                EntityUtils.consume(entity2);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ArchiveIsHtmlParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ArchiveIsHtmlParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseStatus + " " + postRespHeaders[0].toString();
    }

}
