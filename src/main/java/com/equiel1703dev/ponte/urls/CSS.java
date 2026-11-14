package com.equiel1703dev.ponte.urls;

import java.net.URL;

import com.equiel1703dev.ponte.PONTE;

public enum CSS {
    ALERT("alert.css");

    private URL url;

    CSS(String path) {
        this.url = PONTE.class.getResource(path);
    }

    public URL getUrl() {
        return this.url;
    }
}
