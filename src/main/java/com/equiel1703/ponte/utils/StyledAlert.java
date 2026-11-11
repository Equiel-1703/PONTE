package com.equiel1703.ponte.utils;

import com.equiel1703.ponte.urls.CSS;

import javafx.scene.control.Alert;

public class StyledAlert extends Alert {
    public StyledAlert(AlertType t) {
        super(t);

        this
            .getDialogPane()
            .getStylesheets()
            .add(
                CSS.ALERT.getUrl().toExternalForm()
            );
    }
}
