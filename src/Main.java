package com.hms;

import com.hms.ui.MainDashboard;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Launches the JavaFX application lifecycle for MainDashboard.
        // Calling it from a separate non-JavaFX main class prevents module path errors in modern Java.
        Application.launch(MainDashboard.class, args);
    }
}