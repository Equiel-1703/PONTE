package com.equiel1703.ponte.listeners;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.control.Button;

public class OnOffButtonListener implements ChangeListener<Boolean> {
    private final Button onOffButton;
    private final PseudoClass onPseudoClass;

    public OnOffButtonListener(Button onOffButton, PseudoClass onPseudoClass) {
        this.onOffButton = onOffButton;
        this.onPseudoClass = onPseudoClass;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        this.onOffButton.pseudoClassStateChanged(this.onPseudoClass, newValue.booleanValue());
    }
}
