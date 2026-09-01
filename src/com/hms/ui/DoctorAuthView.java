package com.hms.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DoctorAuthView extends Stage {

    private static final String PRIMARY_TEAL = "#0D9488";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String BG_INPUT = "#F9FAFB";
    private static final String BORDER_COLOR = "#E5E7EB";

    private static final String INPUT_STYLE = "-fx-background-color: " + BG_INPUT + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12; -fx-font-size: 14px; -fx-text-fill: " + TEXT_MAIN + ";";
    private static final String INPUT_ERROR_STYLE = "-fx-background-color: #F0FDF4; -fx-border-color: #EF4444; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12; -fx-font-size: 14px;";
    private static final String LABEL_STYLE = "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + ";";

    private boolean isAuthenticated = false;
    private String authenticatedEmail = null;

    public DoctorAuthView(Stage owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Physician Authentication");
        setResizable(false);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #FFFFFF;");

        root.getChildren().addAll(buildHeader(), buildLoginView());

        Scene scene = new Scene(root, 420, 480);
        setScene(scene);
    }

    private VBox buildHeader() {
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(40, 20, 20, 20));

        Label title = new Label("Doctor Login");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_MAIN));

        Label subTitle = new Label("Secure Physician Access");
        subTitle.setFont(Font.font("Segoe UI", 14));
        subTitle.setTextFill(Color.web(TEXT_MUTED));

        header.getChildren().addAll(title, subTitle);
        return header;
    }

    private VBox buildLoginView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20, 45, 40, 45));
        container.setStyle("-fx-background-color: #FFFFFF;");

        VBox emailBox = new VBox(6);
        Label lblEmail = new Label("STAFF EMAIL OR ID");
        lblEmail.setStyle(LABEL_STYLE);
        TextField txtEmail = new TextField();
        txtEmail.setStyle(INPUT_STYLE);
        txtEmail.setPromptText("dr.name@medicare.com");
        emailBox.getChildren().addAll(lblEmail, txtEmail);

        VBox passBox = new VBox(6);
        Label lblPass = new Label("SECURITY PASSWORD");
        lblPass.setStyle(LABEL_STYLE);
        PasswordField txtPass = new PasswordField();
        txtPass.setStyle(INPUT_STYLE);
        passBox.getChildren().addAll(lblPass, txtPass);

        Button btnLogin = new Button("Authorize Access");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: " + PRIMARY_TEAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        VBox.setMargin(btnLogin, new Insets(15, 0, 0, 0));

        btnLogin.setOnAction(e -> {
            boolean isValid = true;
            if (txtEmail.getText().trim().isEmpty()) {
                txtEmail.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                txtEmail.setStyle(INPUT_STYLE);
            }

            if (txtPass.getText().trim().isEmpty()) {
                txtPass.setStyle(INPUT_ERROR_STYLE);
                isValid = false;
            } else {
                txtPass.setStyle(INPUT_STYLE);
            }

            if (!isValid) return;

            btnLogin.setText("Verifying Credentials...");
            btnLogin.setDisable(true);

            // TODO: Connect to DoctorService for real authentication later
            isAuthenticated = true;
            authenticatedEmail = txtEmail.getText().trim();
            close();
        });

        container.getChildren().addAll(emailBox, passBox, btnLogin);
        return container;
    }

    public boolean isAuthenticated() { return isAuthenticated; }
    public String getAuthenticatedEmail() { return authenticatedEmail; }
}