package com.eu.wiki.api;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Admin on 29-Sep-16.
 */
public class Reference {
    private String title;
    private URL link;

    public Reference(String title, URL link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public URL getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(URL link) {
        this.link = link;
    }


}
