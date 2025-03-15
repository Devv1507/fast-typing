package com.example.mainClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class EclipseProgress {
    private final IntegerProperty progress = new SimpleIntegerProperty(100);

    public int getProgress() {
        return progress.get();
    }

    public IntegerProperty progressProperty() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress.set(progress);
    }
}