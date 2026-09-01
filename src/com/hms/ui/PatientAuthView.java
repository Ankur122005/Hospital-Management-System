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

public class PatientAuthView extends Stage {

    // Modern Color Palette
    private static final String PRIMARY_BLUE = "#2563EB";
    private static final String TEXT_MAIN = "#111827";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String BG_INPUT = "#F9FAFB";
    private static final String BORDER_COLOR = "#E5E7EB";

    private static final String INPUT_STYLE = "-fx-background-color: " + BG_INPUT + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12; -fx-font-size: 14px; -fx-text-fill: " + TEXT_MAIN + ";";
    private static final String INPUT_ERROR_STYLE = "-fx-background-color: #FEF2F2; -fx-border-color: #EF4444; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10 12; -fx-font-size: 14px;";
    private static final String LABEL_STYLE = "-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + TEXT_MUTED + ";";

    private boolean isAuthenticated = false;
    private String authenticatedEmail = null;

    private VBox loginView;
    private VBox registerView;
    private StackPane contentArea;

    public PatientAuthView(Stage owner) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Patient Portal Authentication");
        setResizable(false);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Build the UI Components
        VBox header = buildHeader();
        HBox customTabs = buildCustomTabs();

        loginView = buildLoginView();
        registerView = buildRegisterView();

        contentArea = new StackPane(registerView, loginView); // Login is on top initially
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        root.getChildren().addAll(header, customTabs, contentArea);

        Scene scene = new Scene(root, 460, 650);
        setScene(scene);
    }

    private VBox buildHeader() {
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30, 20, 20, 20));

        Label title = new Label("Patient Portal");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(TEXT_MAIN));

        Label subTitle = new Label("Access your medical records and appointments");
        subTitle.setFont(Font.font("Segoe UI", 14));
        subTitle.setTextFill(Color.web(TEXT_MUTED));

        header.getChildren().addAll(title, subTitle);
        return header;
    }

    private HBox buildCustomTabs() {
        HBox tabContainer = new HBox();
        tabContainer.setStyle("-fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 0 1 0;");

        Button btnTabLogin = new Button("Sign In");
        Button btnTabRegister = new Button("Create Account");

        styleTabButton(btnTabLogin, true);
        styleTabButton(btnTabRegister, false);

        btnTabLogin.setOnAction(e -> {
            styleTabButton(btnTabLogin, true);
            styleTabButton(btnTabRegister, false);
            loginView.toFront();
        });

        btnTabRegister.setOnAction(e -> {
            styleTabButton(btnTabRegister, true);
            styleTabButton(btnTabLogin, false);
            registerView.toFront();
        });

        tabContainer.getChildren().addAll(btnTabLogin, btnTabRegister);
        return tabContainer;
    }

    private void styleTabButton(Button btn, boolean isActive) {
        btn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setFont(Font.font("Segoe UI", isActive ? FontWeight.BOLD : FontWeight.NORMAL, 14));
        btn.setTextFill(Color.web(isActive ? PRIMARY_BLUE : TEXT_MUTED));

        String border = isActive ? "-fx-border-color: " + PRIMARY_BLUE + "; -fx-border-width: 0 0 3 0;" : "-fx-border-color: transparent;";
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 12 0; -fx-cursor: hand; " + border);
    }

    private VBox buildLoginView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(35, 45, 35, 45));
        container.setStyle("-fx-background-color: #FFFFFF;");

        VBox emailBox = new VBox(6);
        Label lblEmail = new Label("EMAIL ADDRESS");
        lblEmail.setStyle(LABEL_STYLE);
        TextField txtEmail = new TextField();
        txtEmail.setStyle(INPUT_STYLE);
        txtEmail.setPromptText("patient@example.com");
        emailBox.getChildren().addAll(lblEmail, txtEmail);

        VBox passBox = new VBox(6);
        Label lblPass = new Label("PASSWORD");
        lblPass.setStyle(LABEL_STYLE);
        PasswordField txtPass = new PasswordField();
        txtPass.setStyle(INPUT_STYLE);
        passBox.getChildren().addAll(lblPass, txtPass);

        Button btnLogin = new Button("Continue to Portal");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        VBox.setMargin(btnLogin, new Insets(10, 0, 0, 0));

        btnLogin.setOnAction(e -> {
            boolean isValid = true;
            if (txtEmail.getText().trim().isEmpty() || !txtEmail.getText().contains("@")) {
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

            btnLogin.setText("Authenticating...");
            btnLogin.setDisable(true);

            // TODO: Call AuthService here
            isAuthenticated = true;
            authenticatedEmail = txtEmail.getText().trim();
            close();
        });

        container.getChildren().addAll(emailBox, passBox, btnLogin);
        return container;
    }

    private VBox buildRegisterView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25, 40, 25, 40));
        container.setStyle("-fx-background-color: #FFFFFF;");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);

        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setPercentWidth(50);
        grid.getColumnConstraints().addAll(cc1, cc1);

        // Inputs
        TextField txtName = createInput("Full Name");
        TextField txtEmail = createInput("Email Address");
        TextField txtPhone = createInput("Phone Number");
        TextField txtAge = createInput("Age");

        ComboBox<String> cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("Male", "Female", "Other");
        cmbGender.setStyle(INPUT_STYLE);
        cmbGender.setMaxWidth(Double.MAX_VALUE);

        PasswordField txtPass = new PasswordField();
        txtPass.setStyle(INPUT_STYLE);

        // Adding to Grid
        grid.add(createLabeledField("FULL NAME", txtName), 0, 0, 2, 1);
        grid.add(createLabeledField("EMAIL ADDRESS", txtEmail), 0, 1, 2, 1);
        grid.add(createLabeledField("PHONE NUMBER", txtPhone), 0, 2);
        grid.add(createLabeledField("AGE", txtAge), 1, 2);
        grid.add(createLabeledField("GENDER", cmbGender), 0, 3);
        grid.add(createLabeledField("SET PASSWORD", txtPass), 1, 3);

        Button btnRegister = new Button("Create Account");
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setStyle("-fx-background-color: " + PRIMARY_BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        btnRegister.setOnAction(e -> {
            if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtPass.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Name, Email, and Password are required.");
                alert.setHeaderText("Missing Information");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account created successfully! You can now sign in.");
            alert.setHeaderText(null);
            alert.showAndWait();

            // Switch back to Login view
            ((Button)((HBox)loginView.getParent().getParent().getChildrenUnmodifiable().get(1)).getChildren().get(0)).fire();
        });

        VBox.setMargin(btnRegister, new Insets(15, 0, 0, 0));
        container.getChildren().addAll(grid, btnRegister);
        return container;
    }

    private TextField createInput(String placeholder) {
        TextField tf = new TextField();
        tf.setStyle(INPUT_STYLE);
        tf.setPromptText(placeholder);
        return tf;
    }

    private VBox createLabeledField(String labelText, Control field) {
        VBox box = new VBox(6);
        Label label = new Label(labelText);
        label.setStyle(LABEL_STYLE);
        box.getChildren().addAll(label, field);
        return box;
    }

    public boolean isAuthenticated() { return isAuthenticated; }
    public String getAuthenticatedEmail() { return authenticatedEmail; }
}