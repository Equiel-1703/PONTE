package com.equiel1703.ponte.urls;

import java.net.URL;

import com.equiel1703.ponte.PONTE;

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
